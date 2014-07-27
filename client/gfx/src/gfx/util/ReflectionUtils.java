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
    Member member = findFieldByName(c, name);
    if(member != null) { return member; }
    
    member = findMethodByName(c, name, argHint);
    if(member != null) { return member; }
    
    member = findMethodByName(c, "set" + capitolizeFirst(name), argHint);
    if(member != null) { return member; }
    
    member = findMethodByName(c, "get" + capitolizeFirst(name), argHint);
    if(member != null) { return member; }
    
    System.err.println("No member by name of " + name + '.');
    return null;
  }
  
  public static Field findFieldByName(Class<?> c, String name) {
    for(Field f : c.getFields()) {
      if(f.getName().equalsIgnoreCase(name)) {
        return f;
      }
    }
    
    return null;
  }
  
  public static Method findMethodByName(Class<?> c, String name, Class<?> argHint) {
    if(argHint != null) {
      try {
        return c.getMethod(name, argHint);
      } catch(NoSuchMethodException e) {
      } catch(SecurityException e) {
        e.printStackTrace();
      }
    }
    
    for(Method m : c.getMethods()) {
      if(m.getName().equalsIgnoreCase(name)) {
        return m;
      }
    }
    
    return null;
  }
}