package malachite.gfx.util;

import static malachite.gfx.util.ReflectionUtils.findMethodOrFieldByName;
import static malachite.gfx.util.StringUtils.snakeToCamel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Objects;

public class BoundMember {
  public final Object object;
  public final Member member;
  
  public BoundMember(Object object, Member member) {
    this.object = Objects.requireNonNull(object);
    this.member = Objects.requireNonNull(member);
  }
  
  public BoundMember(Object object, String path, Type type) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    BoundMember bm = null;
    
    switch(type) {
      case ACCESSOR:
        bm = set(object, path, true, false, true, false);
        break;
        
      case MUTATOR:
        bm = set(object, path, true, false, false, true);
        break;
        
      case NEITHER:
        bm = set(object, path, true, false, false, false);
        break;
    }
    
    this.object = bm.object;
    this.member = bm.member;
  }
  
  private BoundMember set(Object object, String path, boolean allowAccessorsInChain, boolean allowMutatorsInChain, boolean allowAccessorsAtEnd, boolean allowMutatorsAtEnd) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    String[] parts = path.split("\\.");
    
    BoundMember bm = null;
    for(int i = 0; i < parts.length; i++) {
      String part = parts[i];
      
      boolean accessors = false;
      boolean mutators  = false;
      
      if(i < parts.length - 1) {
        accessors = allowAccessorsInChain;
        mutators  = allowMutatorsInChain;
      } else {
        accessors = allowAccessorsAtEnd;
        mutators  = allowMutatorsAtEnd;
      }
      
      Object o = bm != null ? bm.getValue() : object;
      bm = new BoundMember(o, findMethodOrFieldByName(o.getClass(), snakeToCamel(part), accessors, mutators));
    }
    
    return bm;
  }
  
  public Object getValue(Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if(member instanceof Method) {
      Method method = (Method)member;
      return method.invoke(object, (Object[])params);
    } else if(member instanceof Field) {
      Field field = (Field)member;
      return field.get(object);
    }
    
    return null;
  }
  
  public void setValue(Object... params) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    if(member instanceof Method) {
      Method method = (Method)member;
      method.invoke(object, (Object[])params);
    } else if(member instanceof Field) {
      Field field = (Field)member;
      field.set(object, params[0]);
    }
  }
  
  public enum Type {
    ACCESSOR, MUTATOR, NEITHER;
  }
}