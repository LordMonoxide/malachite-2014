package gfx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import static gfx.util.StringUtils.*;

public class ReflectionUtils {
  public static Member findMethodOrFieldByName(Class<?> c, String name) {
    return findMethodOrFieldByName(c, name, null);
  }
  
  public static Member findMethodOrFieldByName(Class<?> c, String name, Class<?> argHint) {
    for(Field f : c.getFields()) {
      if(f.getName().equalsIgnoreCase(name)) {
        return f;
      }
    }
    
    String methodName = "set" + capitolizeFirst(name);
    
    if(argHint != null) {
      try {
        return c.getMethod(methodName, argHint);
      } catch(NoSuchMethodException e) {
      } catch(SecurityException e) {
        e.printStackTrace();
      }
    }
    
    for(Method m : c.getMethods()) {
      if(m.getName().equalsIgnoreCase(methodName)) {
        return m;
      }
    }
    
    System.err.println("No method by name of " + methodName + '.');
    
    return null;
  }
}