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
          GUIParser parser = new GUIParser();
          GUI gui = null;
          
          try {
            gui = parser.loadFromFile(Paths.get("../data/gfx/guis/mainmenu.json"));
          } catch(IllegalArgumentException | JSONException | SecurityException | IOException e) {
            e.printStackTrace();
          }
          
          gui.push();
        }
      });
    });
    
    _context.run();
  }
  
  private GUI _gui;
  private List<AssignLater> _assignLater = new ArrayList<>();
  private Map<String, Control<?>> _controls = new HashMap<>();
  
  public GUI loadFromFile(Path f) throws IOException, JSONException, SecurityException, IllegalArgumentException {
    byte[] raw = Files.readAllBytes(f);
    String data = new String(raw);
    return load(new JSONObject(data));
  }
  
  public GUI load(JSONObject json) throws JSONException, SecurityException, IllegalArgumentException {
    _gui = new GUI() {
      @Override protected void resize() {
        
      }
      
      @Override protected boolean logic() {
        return false;
      }
      
      @Override protected void load() {
        for(String key : json.keySet()) {
          if(key.equals("controls")) {
            try {
              addControls(_gui.controls(), json.getJSONObject("controls"));
            } catch(InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | JSONException e) {
              e.printStackTrace();
            }
          }
        }
        
        try {
          processLateAssignment();
        } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | ClassNotFoundException | NoSuchMethodException | SecurityException | JSONException e) {
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
  
  private void addControls(ControlList controls, JSONObject json) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
    for(String name : json.keySet()) {
      JSONObject attribs = json.getJSONObject(name);
      
      String controlType;
      
      try {
        controlType = attribs.getString("type");
      } catch(JSONException e) {
        controlType = attribs.getString("Type");
      }
      
      controlType = snakeToProper(controlType);
      
      Control<?> c = Class.forName("gfx.gui.control." + controlType).asSubclass(Control.class).newInstance();
      controls.add(c);
      _controls.put(name, c);
      
      parseAttribs(c, attribs);
    }
  }
  
  private void parseAttribs(Object c, JSONObject attribs) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, JSONException {
    for(String attrib : attribs.keySet()) {
      switch(attrib.toLowerCase()) {
        case "type": break;
        case "controls":
          if(!(c instanceof Control)) {
            System.err.println("Tried to add controls to a non-control");
            return;
          }
          
          addControls(((Control<?>)c).controls(), attribs.getJSONObject(attrib));
          break;
          
        default:
          parseAttrib(c, attrib, attribs.get(attrib));
          break;
      }
    }
  }
  
  private void parseAttrib(Object obj, String attrib, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException {
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
      System.err.println("Couldn't find field/method " + attrib + "!");
    }
  }
  
  private void assignValue(Object obj, Member member, Object value) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, JSONException {
    if(member instanceof Field) {
      if(!(value instanceof JSONObject)) {
        System.err.println("Value of a field must be a JSONObject");
        return;
      }
      
      parseAttribs(((Field)member).get(obj), (JSONObject)value);
    } else if(member instanceof Method) {
      Method method = (Method)member;
      method.invoke(obj, value);
    }
  }
  
  private void processLateAssignment() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException {
    for(AssignLater late : _assignLater) {
      String[] parts = late.value.substring(1).split("\\.");
      
      Object value = null;
      for(String part : parts) {
        if(value == null) {
          value = _controls.get(part);
        } else {
          Member member = findMethodOrFieldByName(value.getClass(), snakeToCamel(part));
          if(member instanceof Method) {
            value = ((Method)member).invoke(value);
          } else if(member instanceof Field) {
            value = ((Field)member).get(value);
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