package malachite.gfx.gui.parser;

import static malachite.gfx.util.StringUtils.snakeToProper;

import java.lang.reflect.InvocationTargetException;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.gfx.gui.Control;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.BoundMember;

public class ParserControl {
  private Parser _parser;
  
  ParserControl(Parser parser, Control<?> parent, JSONObject attribs) throws ParserException {
    _parser = parser;
    
    String type = getClassNameFromAttribs(attribs);
    Control<?> control = createControlInstance(type);
    parent.controls().add(control);
    parseAttribs(control, attribs, "type");
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
  
  private void parseAttribs(Object object, JSONObject attribs, String... ignores) throws ParserException {
    for(String attrib : attribs.keySet()) {
      boolean skip = false;
      
      for(String ignore : ignores) {
        if(attrib.equals(ignore)) {
          skip = true;
          break;
        }
      }
      
      if(skip) {
        continue;
      }
      
      switch(attrib.toLowerCase()) {
        case "controls":
          if(!(object instanceof Control)) {
            parseAttrib(object, attrib, attribs.get(attrib));
            break;
          }
          
          try {
            _parser.parseControls((Control<?>)object, attribs.getJSONObject(attrib));
          } catch(JSONException e) {
            throw new ParserException.SyntaxException(e);
          }
          
          break;
          
        case "events":
          if(_parser._events == null) {
            parseAttrib(object, attrib, attribs.get(attrib));
            break;
          }
          
          if(!(object instanceof Control)) {
            parseAttrib(object, attrib, attribs.get(attrib));
            break;
          }
          
          try {
            ///////////parseEvents((Control<?>)object, attribs.getJSONObject(attrib));
          } catch(JSONException e) {
            throw new ParserException.SyntaxException(e);
          }
          
          break;
          
        default:
          parseAttrib(object, attrib, attribs.get(attrib));
          break;
      }
    }
  }
  
  private void parseAttrib(Object object, String attrib, Object value) throws ParserException {
    /*
    // Deduce type
    Class<?> type = value.getClass();
    if(type == Integer.class) { type = int.class; }
    if(type == Boolean.class) { type = boolean.class; }
    
    Member member = findMethodOrFieldByName(object.getClass(), snakeToCamel(attrib), type);
    
    if(member != null) {
      if(type == String.class) {
        String s = (String)value;
        if(s.startsWith("@")) {
          ////////_assignLater.add(new AssignLater(object, member, memberFromPath(s, true, true)));
          return;
        }
        
        if(s.startsWith("#")) {
          String path = s.substring(1).replace('.', '/') + ".png";
          value = TextureBuilder.getInstance().getTexture(path);
          type = Texture.class;
        }
      }
      
      //////////assignValue(object, member, value);
    } else {
      throw new ParserException.NoSuchMemberException(object, attrib, null);
    }
    */
    
    BoundMember bm = null;
    Object val = value;
    
    try {
      bm = new BoundMember(object, attrib, BoundMember.Type.MUTATOR);
      
      if(val instanceof String) {
        String s = (String)val;
        
        if(s.startsWith("@")) {
          ////////_assignLater.add(new AssignLater(object, member, memberFromPath(s, true, true)));
          return;
        } else if(s.startsWith("#")) {
          String path = s.substring(1).replace('.', '/') + ".png";
          val = TextureBuilder.getInstance().getTexture(path);
        }
      }
      
      bm.setValue(val);
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      e.printStackTrace();
    } catch(NoSuchMethodException e) {
      throw new ParserException.NoSuchMemberException(object, attrib, null);
    }
  }
}