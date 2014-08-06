package malachite.gfx.gui.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import malachite.gfx.gui.GUI;

import org.json.JSONObject;

public class Parser {
  private GUI _gui;
  private Object _events;
  
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
        ready();
      }
      
      @Override protected void load() {
        
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
}