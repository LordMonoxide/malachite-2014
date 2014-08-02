package malachite.gfx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static malachite.gfx.util.StringUtils.*;

public class ReflectionUtils {
  public static Member findMethodOrFieldByName(Class<?> c, String name, Class<?>... argHint) {
    return findMethodOrFieldByName(c, name, true, true, argHint);
  }
  
  public static Member findMethodOrFieldByName(Class<?> c, String name, boolean withGets, boolean withSets, Class<?>... argHint) {
    Member member = findFieldByName(c, name);
    if(member != null) { return member; }
    
    member = findMethodByName(c, name, argHint);
    if(member != null) { return member; }
    
    if(withSets) {
      member = findMethodByName(c, "set" + capitolizeFirst(name), argHint);
      if(member != null) { return member; }
    }
    
    if(withGets) {
      member = findMethodByName(c, "get" + capitolizeFirst(name), argHint);
      if(member != null) { return member; }
    }
    
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
  
  public static Method findMethodByName(Class<?> c, String name, Class<?>... argHint) {
    if(argHint != null) {
      try {
        return c.getMethod(name, argHint);
      } catch(NoSuchMethodException e) {
        //System.out.println("No " + name + " with " + argHint);
      } catch(SecurityException e) {
        e.printStackTrace();
      }
    }
    
    List<MethodAccuracy> methods = new ArrayList<>();
    
    for(Method m : c.getMethods()) {
      boolean methodExists = false;
      
      int total = 100;
      
      if(m.getName().equals(name)) {
        methodExists = true;
      } else if(m.getName().equalsIgnoreCase(name)) {
        methodExists = true;
        total -= 2;
      }
      
      if(methodExists) {
        total -= java.lang.Math.abs(argHint.length - m.getParameterCount()) * 5;
        
        for(int i = 0; i < java.lang.Math.min(argHint.length, m.getParameterCount()); i++) {
          if(argHint[i] != m.getParameterTypes()[i] && argHint[i].isAssignableFrom(m.getParameterTypes()[i])) {
            total -= 2;
          }
        }
        
        methods.add(new MethodAccuracy(m, total));
      }
    }
    
    MethodAccuracy mostAccurate = null;
    for(MethodAccuracy m : methods) {
      if(mostAccurate == null) {
        mostAccurate = m;
      } else {
        if(m.accuracy > mostAccurate.accuracy) {
          mostAccurate = m;
        }
      }
    }
    
    if(mostAccurate != null) {
      return mostAccurate.method;
    }
    
    return null;
  }
  
  private static class MethodAccuracy {
    public final Method method;
    public final int    accuracy;
    
    public MethodAccuracy(Method method, int accuracy) {
      this.method   = method;
      this.accuracy = accuracy;
    }
  }
}