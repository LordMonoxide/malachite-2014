package malachite.engine.conf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;

public class Conf {
  private final Path _file;
  private JSONObject _root;
  
  public Conf(String file) throws JSONException, IOException {
    _file = Paths.get(file);
    
    if(Files.exists(_file)) {
      load();
    } else {
      _root = new JSONObject();
    }
  }
  
  public <T> void set(String path, T val) {
    String[] parts = tokenize(path);
    String key = parts[parts.length - 1];
    JSONObject obj = getObjectFromPath(parts);
    obj.put(key, val);
  }
  
  public Object get(String path) {
    String[] parts = tokenize(path);
    String key = parts[parts.length - 1];
    JSONObject obj = getObjectFromPath(parts);
    return obj.opt(key);
  }
  
  public void load() throws JSONException, IOException {
    _root = new JSONObject(new String(Files.readAllBytes(_file), StandardCharsets.UTF_8));
  }
  
  public void save() throws JSONException, IOException {
    Files.write(_file, _root.toString(2).getBytes(StandardCharsets.UTF_8));
  }
  
  public boolean delete() throws IOException {
    return Files.deleteIfExists(_file);
  }
  
  public String[] tokenize(String key) {
    return key.split("\\.");
  }
  
  public JSONObject getObjectFromPath(String keys[]) {
    JSONObject obj = _root;
    
    for(int i = 0; i < keys.length - 1; i++) {
      String key = keys[i];
      
      if(!obj.has(key)) {
        obj.put(key, new JSONObject());
      }
      
      obj = obj.getJSONObject(key);
    }
    
    return obj;
  }
}