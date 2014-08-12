package malachite.gfx.gui.parser;

import static malachite.gfx.util.StringUtils.snakeToProper;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.gfx.gui.Control;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.BoundMember;

public class ParserControl {
  private Parser _parser;
  private Control<?> _control;
  
  private List<LateAssignment> _lateAssignments = new ArrayList<>();
  
  ParserControl(Parser parser, Control<?> parent, String name, JSONObject attribs) throws ParserException {
    _parser = parser;
    
    String type = getClassNameFromAttribs(attribs);
    Control<?> control = createControlInstance(type);
    parent.controls().add(control);
    _control = control;
    _parser._controls.put(name, this);
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
    BoundMember bm = null;
    Object val = value;
    
    try {
      bm = new BoundMember(object, attrib, BoundMember.Type.MUTATOR);
      
      if(val instanceof String) {
        String s = (String)val;
        
        if(s.startsWith("@")) {
          _lateAssignments.add(new LateAssignment(bm, boundMemberFromMemberPath(s)));
          return;
        } else if(s.startsWith("#")) {
          String path = s.substring(1).replace('.', '/') + ".png";
          val = TextureBuilder.getInstance().getTexture(path);
        }
      }
      
      bm.setValue(val);
    } catch(IllegalAccessException e) {
      //TODO
      e.printStackTrace();
    } catch(IllegalArgumentException e) {
      //TODO
      e.printStackTrace();
    } catch(InvocationTargetException e) {
      //TODO
      e.printStackTrace();
    } catch(NoSuchMethodException e) {
      throw new ParserException.NoSuchMemberException(object, attrib, null);
    }
  }
  
  private BoundMember boundMemberFromMemberPath(String path) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
    String name = path.substring(1, path.indexOf('.'));
    Control<?> control = _parser._controls.get(name)._control;
    return new BoundMember(control, path.substring(path.indexOf('.') + 1), BoundMember.Type.ACCESSOR);
  }
  
  public void processLateAssignments() throws ParserException {
    for(LateAssignment late : _lateAssignments) {
      late.assign();
    }
  }
}