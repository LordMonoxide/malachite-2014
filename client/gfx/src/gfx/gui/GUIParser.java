package gfx.gui;

import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

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
            gui = parser.load(new JSONObject("{controls:{Textbox:{Text:\"Test\",W:200,H:20}}}"));
          } catch(IllegalArgumentException | JSONException | SecurityException e) {
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
              addControls(_gui.controls(), json.getJSONObject("controls"));
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
  
  private void addControls(ControlList controls, JSONObject json) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
    for(String key : json.keySet()) {
      Control<?> c = Class.forName("gfx.gui.control." + key).asSubclass(Control.class).newInstance();
      controls.add(c);
      
      JSONObject control = json.getJSONObject(key);
      for(String attrib : control.keySet()) {
        if(attrib.equals("controls")) {
          addControls(c.controls(), control.getJSONObject(attrib));
        } else {
          Object value = control.get(attrib);
          
          Class<?> type = value.getClass();
          if(type == Integer.class) { type = int.class; }
          if(type == Boolean.class) { type = boolean.class; }
          
          Method method = c.getClass().getMethod("set" + attrib, new Class<?>[] { type });
          method.invoke(c, value);
        }
      }
    }
  }
}