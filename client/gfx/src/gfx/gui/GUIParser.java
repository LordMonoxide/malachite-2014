package gfx.gui;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GUIParser {
  private GUI _gui;
  
  public GUI loadFromFile(Path f) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, NoSuchMethodException, SecurityException {
    byte[] raw = Files.readAllBytes(f);
    String data = new String(raw);
    return load(new JSONObject(data));
  }
  
  public GUI load(JSONObject json) throws InstantiationException, IllegalAccessException, ClassNotFoundException, JSONException, NoSuchMethodException, SecurityException {
    _gui = new GUI() {
      @Override protected void resize() {
        
      }
      
      @Override protected boolean logic() {
        return false;
      }
      
      @Override protected void load() {
        
      }
      
      @Override protected void draw() {
        
      }
      
      @Override public void destroy() {
        
      }
    };
    
    for(String key : json.keySet()) {
      if(key.equals("controls")) {
        addControls(_gui.controls(), json.getJSONObject("controls"));
      }
    }
    
    return _gui;
  }
  
  private void addControls(ControlList controls, JSONObject json) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, SecurityException {
    for(String key : json.keySet()) {
      Control<?> c = Class.forName("gfx.gui." + key).asSubclass(Control.class).newInstance();
      
      for(String attrib : json.getJSONObject(key).keySet()) {
        Method method = c.getClass().getMethod(attrib, new Class<?>[] { });
      }
    }
  }
}