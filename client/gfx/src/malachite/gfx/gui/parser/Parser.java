package malachite.gfx.gui.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import malachite.gfx.gui.Control;
import malachite.gfx.gui.GUI;

import org.json.JSONObject;

public class Parser {
  GUI        _gui;
  Control<?> _root;
  Object     _events;
  
  private Map<String, ParserControl> _controls = new HashMap<>();
  
  public GUI loadFromFile(Path f) throws IOException { return loadFromFile(f, null); }
  public GUI loadFromFile(Path f, Object gateway) throws IOException {
    byte[] raw = Files.readAllBytes(f);
    String data = new String(raw);
    return load(new JSONObject(data), gateway);
  }
  
  public GUI load(JSONObject json) { return load(json, null); }
  public GUI load(JSONObject json, Object events) {
    class ParserGUI extends GUI {
      private ParserGUI() {
        _root = _control;
        ready();
      }
      
      @Override protected void load() {
        try {
          parseGUIAttribs(json);
        } catch(ParserException e) {
          e.printStackTrace();
        }
      }
      
      @Override public void destroy() {
        
      }
      
      @Override protected void resize() {
        
      }
      
      @Override protected void draw() {
        
      }
      
      @Override protected boolean logic() {
        return false;
      }
    };
    
    _events = events;
    _gui = new ParserGUI();
    
    return _gui;
  }
  
  void parseGUIAttribs(JSONObject attribs) throws ParserException {
    for(String key : attribs.keySet()) {
      parseGUIAttrib(attribs, key);
    }
  }
  
  void parseGUIAttrib(JSONObject attribs, String key) throws ParserException {
    switch(key) {
      case "controls":
        parseControls(_root, attribs.getJSONObject(key));
        break;
    }
  }
  
  void parseControls(Control<?> parent, JSONObject controls) throws ParserException {
    for(String name : controls.keySet()) {
      _controls.put(name, new ParserControl(this, parent, controls.getJSONObject(name)));
    }
  }
}