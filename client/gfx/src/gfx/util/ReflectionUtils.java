package gfx.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import static gfx.util.StringUtils.*;

public class ReflectionUtils {
  public static Member findMethodOrFieldByName(Class<?> c, String name) {
    for(Field f : c.getFields()) {
      if(f.getName().equals(name)) {
        return f;
      }
    }
    
    String methodName = "set" + capitolizeFirst(name);
    for(Method m : c.getMethods()) {
      if(m.getName().equals(methodName)) {
        return m;
      }
    }
    
    return null;
  }
}