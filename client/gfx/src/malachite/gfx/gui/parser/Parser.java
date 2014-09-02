package malachite.gfx.gui.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import malachite.engine.lang.Lang;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.GUI;

import org.json.JSONObject;

public class Parser {
  final Lang _lang;
  
  GUI        _gui;
  Control<?> _root;
  GUIEvents  _events;
  
  Map<String, ParserControl> _controls = new HashMap<>();
  
  public Parser(Lang lang) {
    _lang = lang;
  }
  
  public GUI loadFromFile(Path f) throws IOException { return loadFromFile(f, null); }
  public GUI loadFromFile(Path f, GUIEvents events) throws IOException {
    byte[] raw = Files.readAllBytes(f);
    String data = new String(raw);
    return load(new JSONObject(data), events);
  }
  
  public GUI load(JSONObject json) { return load(json, null); }
  public GUI load(JSONObject json, GUIEvents events) {
    class ParserGUI extends GUI {
      private ParserGUI() {
        _root = _control;
        ready();
      }
      
      @Override protected void load() {
        try {
          parseGUIAttribs(json);
          
          Map<String, Control<?>> controls = new HashMap<>(_controls.size());
          for(Map.Entry<String, ParserControl> e : _controls.entrySet()) {
            controls.put(e.getKey(), e.getValue().control);
          }
          
          events.registerControls(Collections.unmodifiableMap(controls));
          
          parseAttribs();
          processLateAssignments();
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
      new ParserControl(this, parent, name, controls.getJSONObject(name));
    }
  }
  
  private void parseAttribs() throws ParserException {
    for(ParserControl control : _controls.values()) {
      control.parseAttribs();
    }
  }
  
  private void processLateAssignments() throws ParserException {
    for(ParserControl control : _controls.values()) {
      control.processLateAssignments();
    }
  }
}