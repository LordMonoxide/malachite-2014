package gfx.gui;

import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;
import gfx.textures.Texture;
import gfx.textures.TextureBuilder;
import static gfx.util.ReflectionUtils.*;
import static gfx.util.StringUtils.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class GUIParser {
  private static Context _context;
  
  public static void main(String[] args) throws JSONException, SecurityException {
    Manager.registerContext(gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          try {
            GUIParser parser = new GUIParser();
            GUI gui = null;
            
            gui = parser.loadFromFile(Paths.get("../data/gfx/guis/mainmenu.json"));
            gui.push();
          } catch(IOException e) {
            e.printStackTrace();
          }
        }
      });
    });
    
    _context.run();
  }
  
  private GUI _gui;
  private GUIGateway _gateway;
  
  private List<AssignLater> _assignLater = new ArrayList<>();
  private Map<String, Control<?>> _controls = new HashMap<>();
  
  public GUI loadFromFile(Path f) throws IOException { return loadFromFile(f, null); }
  public GUI loadFromFile(Path f, GUIGateway gateway) throws IOException {
    byte[] raw = Files.readAllBytes(f);
    String data = new String(raw);
    return load(new JSONObject(data), gateway);
  }
  
  public GUI load(JSONObject json) { return load(json, null); }
  public GUI load(JSONObject json, GUIGateway gateway) {
    _gateway = gateway;
    _gui = new GUI() {
      @Override protected void resize() {
        
      }
      
      @Override protected boolean logic() {
        return false;
      }
      
      @Override protected void load() {
        try {
          for(String key : json.keySet()) {
            if(key.equals("controls")) {
              try {
                parseControls(_gui.controls(), json.getJSONObject("controls"));
              } catch(JSONException e) {
                throw new GUIParserException.SyntaxException(e);
              }
            }
          }
          
          processLateAssignment();
        } catch(GUIParserException e) {
          e.printStackTrace();
        }
      }
      
      @Override protected void draw() {
        
      }
      
      @Override public void destroy() {
        
      }
    };
    
    _gui.ready();
    return _gui;
  }
  
  private void parseControls(ControlList controls, JSONObject json) throws GUIParserException {
    for(String name : json.keySet()) {
      JSONObject attribs = json.getJSONObject(name);
      
      String controlType;
      
      try {
        controlType = attribs.getString("type");
      } catch(JSONException e) {
        controlType = attribs.getString("Type");
      }
      
      controlType = snakeToProper(controlType);
      
      Control<?> c;
      
      try {
        c = Class.forName("gfx.gui.control." + controlType).asSubclass(Control.class).newInstance();
      } catch(InstantiationException | IllegalAccessException e) {
        throw new GUIParserException.EngineException(e);
      } catch(ClassNotFoundException e) {
        throw new GUIParserException.NoSuchControlException(controlType, e);
      }
      
      controls.add(c);
      _controls.put(name, c);
      
      parseAttribs(c, attribs);
    }
  }
  
  private void parseEvents(Control<?> control, JSONObject json) throws GUIParserException {
    
  }
  
  private void parseAttribs(Object c, JSONObject attribs) throws GUIParserException {
    for(String attrib : attribs.keySet()) {
      switch(attrib.toLowerCase()) {
        case "type": break;
        case "controls":
          if(!(c instanceof Control)) {
            parseAttrib(c, attrib, attribs.get(attrib));
            break;
          }
          
          try {
            parseControls(((Control<?>)c).controls(), attribs.getJSONObject(attrib));
          } catch(JSONException e) {
            throw new GUIParserException.SyntaxException(e);
          }
          
          break;
          
        case "events":
          if(!(c instanceof Control)) {
            parseAttrib(c, attrib, attribs.get(attrib));
            break;
          }
          
          try {
            parseEvents((Control<?>)c, attribs.getJSONObject(attrib));
          } catch(JSONException e) {
            throw new GUIParserException.SyntaxException(e);
          }
          
          break;
          
        default:
          parseAttrib(c, attrib, attribs.get(attrib));
          break;
      }
    }
  }
  
  private void parseAttrib(Object obj, String attrib, Object value) throws GUIParserException {
    // Deduce type
    Class<?> type = value.getClass();
    if(type == Integer.class) { type = int.class; }
    if(type == Boolean.class) { type = boolean.class; }
    
    Member member = findMethodOrFieldByName(obj.getClass(), snakeToCamel(attrib), type);
    
    if(member != null) {
      if(type == String .class) {
        String s = (String)value;
        if(s.startsWith("@")) {
          _assignLater.add(new AssignLater(obj, member, (String)value));
          return;
        }
        
        if(s.startsWith("#")) {
          String path = s.substring(1).replace('.', '/') + ".png";
          value = TextureBuilder.getInstance().getTexture(path);
          type = Texture.class;
        }
      }
      
      assignValue(obj, member, value);
    } else {
      throw new GUIParserException.NoSuchMemberException(obj, attrib, null);
    }
  }
  
  private void assignValue(Object obj, Member member, Object value) throws GUIParserException {
    try {
      if(member instanceof Field) {
        if(!(value instanceof JSONObject)) {
          throw new GUIParserException.SyntaxException("Value of " + obj + "'s " + member + " should be a JSON object, but was " + value + " instead.", null);
        }
        
        parseAttribs(((Field)member).get(obj), (JSONObject)value);
      } else if(member instanceof Method) {
        Method method = (Method)member;
        method.invoke(obj, value);
      }
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new GUIParserException.EngineException(e);
    }
  }
  
  private void processLateAssignment() throws GUIParserException {
    for(AssignLater late : _assignLater) {
      String[] parts = late.value.substring(1).split("\\.");
      
      Object value = null;
      for(String part : parts) {
        if(value == null) {
          value = _controls.get(part);
        } else {
          try {
            Member member = findMethodOrFieldByName(value.getClass(), snakeToCamel(part));
            if(member instanceof Method) {
              value = ((Method)member).invoke(value);
            } else if(member instanceof Field) {
              value = ((Field)member).get(value);
            }
          } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new GUIParserException.EngineException(e);
          }
        }
      }
      
      assignValue(late.obj, late.member, value);
    }
  }
  
  private class AssignLater {
    public Object obj;
    public Member member;
    public String value;
    
    public AssignLater(Object obj, Member member, String value) {
      this.obj = obj;
      this.member = member;
      this.value = value;
    }
  }
}