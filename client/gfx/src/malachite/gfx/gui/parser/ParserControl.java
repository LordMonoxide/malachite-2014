package malachite.gfx.gui.parser;

import static malachite.gfx.util.ReflectionUtils.findMethodByName;
import static malachite.gfx.util.StringUtils.snakeToCamel;
import static malachite.gfx.util.StringUtils.snakeToProper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.BoundMember;

public class ParserControl {
  private Parser _parser;
  public final Control<?> control;
  private JSONObject _attribs;
  
  private List<LateAssignment> _lateAssignments = new ArrayList<>();
  
  private Pattern _eventParser = Pattern.compile("^(\\w+)(?:\\(((?:@[\\w\\.]+,? ?)+)\\))?$");
  
  ParserControl(Parser parser, Control<?> parent, String name, JSONObject attribs) throws ParserException {
    _parser = parser;
    
    String type = getClassNameFromAttribs(attribs);
    Control<?> control = createControlInstance(type);
    parent.controls().add(control);
    this.control = control;
    _parser._controls.put(name, this);
    _attribs = attribs;
    parseControls();
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
  
  private void parseControls() throws ParserException {
    if(_attribs.has("controls")) {
      _parser.parseControls(control, _attribs.getJSONObject("controls"));
    }
  }
  
  public void parseAttribs() throws ParserException {
    parseAttribs("type", "controls");
  }
  
  private void parseAttribs(String... ignores) throws ParserException {
    for(String attrib : _attribs.keySet()) {
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
        case "events":
          if(_parser._events == null) {
            parseAttrib(attrib, _attribs.get(attrib));
            break;
          }
          
          try {
            parseEvents(_attribs.getJSONObject(attrib));
          } catch(JSONException e) {
            throw new ParserException.SyntaxException(e);
          }
          
          break;
          
        default:
          parseAttrib(attrib, _attribs.get(attrib));
          break;
      }
    }
  }
  
  private void parseAttrib(String attrib, Object value) throws ParserException {
    BoundMember bm = null;
    Object val = value;
    
    try {
      bm = new BoundMember(control, attrib, BoundMember.Type.MUTATOR, val.getClass());
      
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
      throw new ParserException.NoSuchMemberException(control, attrib, null);
    }
  }
  
  private BoundMember boundMemberFromMemberPath(String path) throws ParserException.EventException {
    String name = path.substring(1, path.indexOf('.'));
    Control<?> control = _parser._controls.get(name).control;
    
    try {
      return new BoundMember(control, path.substring(path.indexOf('.') + 1), BoundMember.Type.ACCESSOR);
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
      throw new ParserException.ErrorParsingEventPathException(e);
    }
  }
  
  public void processLateAssignments() throws ParserException {
    for(LateAssignment late : _lateAssignments) {
      late.assign();
    }
  }
  
  private void parseEvents(JSONObject events) throws ParserException {
    for(String name : events.keySet()) {
      Event event = parseEventString(events.getString(name));
      
      Method controlEvent = findMethodByName(control.events().getClass(), snakeToCamel(name),       ControlEvents.Event.class);
      Method callback     = findMethodByName(_parser._events .getClass(), snakeToCamel(event.name), ControlEvents.EventData.class);
      
      if(controlEvent == null) {
        throw new ParserException.InvalidCallbackException(name, control.getClass().getName(), null);
      }
      
      if(callback == null) {
        throw new ParserException.NoEventListenerException(event.name, control.getClass().getName(), null);
      }
      
      callback.setAccessible(true);
      Class<?> type = controlEvent.getParameters()[0].getType();
      
      Object o = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { type }, new InvocationHandler() {
        @Override public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          if(event.arguments == null) {
            callback.invoke(_parser._events, args);
          } else {
            Object[] objs = new Object[event.arguments.length];
            for(int i = 0; i < event.arguments.length; i++) {
              objs[i] = event.arguments[i].getValue();
            }
            
            callback.invoke(_parser._events, objs);
          }
          
          return null;
        }
      });
      
      try {
        controlEvent.invoke(control.events(), o);
      } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new ParserException.EngineException(e);
      }
    }
  }
  
  private Event parseEventString(String eventString) throws ParserException {
    Matcher matcher = _eventParser.matcher(eventString);
    
    String        fn   = null;
    BoundMember[] args = null;
    
    if(matcher.find()) {
      fn = matcher.group(1);
      
      String a = matcher.group(2);
      if(a != null) {
        String[] a2 = a.split(", ?");
        args = new BoundMember[a2.length];
        
        for(int i = 0; i < a2.length; i++) {
          args[i] = boundMemberFromMemberPath(a2[i]);
        }
      }
    } else {
      throw new ParserException.SyntaxException("Invalid event syntax", null);
    }
    
    return new Event(fn, args);
  }
  
  private class Event {
    public String name;
    public BoundMember[] arguments;
    
    public Event(String name, BoundMember[] arguments) {
      this.name = name;
      this.arguments = arguments;
    }
  }
}