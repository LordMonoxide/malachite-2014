package malachite.gfx.gui.parser;

import static malachite.gfx.util.StringUtils.snakeToProper;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.gfx.gui.Control;

public class ParserControl {
  ParserControl(Control<?> parent, JSONObject attribs) throws ParserException {
    String type = getClassNameFromAttribs(attribs);
    Control<?> control = createControlInstance(type);
    parent.controls().add(control);
  }
  
  private String getClassNameFromAttribs(JSONObject attribs) throws ParserException {
    String type;
    
    // Check to see what type of control they're asking for
    //TODO: Figure out a way to do this case-insensitively
    try {
      type = attribs.getString("type");
    } catch(JSONException e) {
      // Bail if they didn't provide a control type
      throw new ParserException.SyntaxException("No type was provided for control", null);
    }
    
    // Convert the camel_case name to ClassCase
    return snakeToProper(type);
  }
  
  private Control<?> createControlInstance(String type) throws ParserException {
    Control<?> control;
    
    // Attempt to create a new control of the type specified
    try {
      control = Class.forName("malachite.gfx.gui.control." + type).asSubclass(Control.class).newInstance();
    } catch(InstantiationException | IllegalAccessException e) {
      // Something fucky happened
      throw new ParserException.EngineException(e);
    } catch(ClassNotFoundException e) {
      // They asked for a Control that doesn't exist
      throw new ParserException.NoSuchControlException(type, e);
    }
    
    return control;
  }
}