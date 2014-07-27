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

import org.json.JSONArray;
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
              addControls(_gui.controls(), json.getJSONArray("controls"));
            } catch(InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | JSONException e) {
              e.printStackTrace();
            }
          }
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
  
  private void addControls(ControlList controls, JSONArray array) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
    for(int i = 0; i < array.length(); i++) {
      JSONObject control = array.getJSONObject(i);
      
      String controlType;
      try {
        controlType = control.getString("type");
      } catch(JSONException e) {
        controlType = control.getString("Type");
      }
      
      controlType = snakeToProper(controlType);
      
      Control<?> c = Class.forName("gfx.gui.control." + controlType).asSubclass(Control.class).newInstance();
      controls.add(c);
      
      parseAttribs(c, control);
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
          
          addControls(((Control<?>)c).controls(), attribs.getJSONArray(attrib));
          break;
          
        default:
          parseAttrib(c, attrib, attribs.get(attrib));
          break;
      }
    }
  }
  
  private void parseAttrib(Object c, String attrib, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchMethodException, SecurityException, JSONException {
    Class<?> type = value.getClass();
    if(type == Integer.class) { type = int.class; }
    if(type == Boolean.class) { type = boolean.class; }
    if(type == String .class) {
      String s = (String)value;
      if(s.startsWith("@")) {
        
      }
      
      if(s.startsWith("#")) {
        String path = s.substring(1).replace('.', '/') + ".png";
        value = TextureBuilder.getInstance().getTexture(path);
        type = Texture.class;
      }
    }
    
    String methodName = snakeToCamel(attrib);
    Member member = findMethodOrFieldByName(c.getClass(), methodName, type);
    
    if(member != null) {
      if(member instanceof Field) {
        if(!(value instanceof JSONObject)) {
          System.err.println("Value of a field must be a JSONObject");
          return;
        }
        
        parseAttribs(((Field)member).get(c), (JSONObject)value);
      } else if(member instanceof Method) {
        Method method = (Method)member;
        method.invoke(c, value);
      }
    } else {
      System.err.println("Couldn't find field/method " + methodName + "!");
    }
  }
}