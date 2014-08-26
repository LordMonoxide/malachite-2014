package malachite.gfx.util;

import static malachite.gfx.util.ReflectionUtils.findMethodOrFieldByName;
import static malachite.gfx.util.StringUtils.snakeToCamel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Objects;

public class BoundMember {
  @Override public String toString() {
    return member + " - bound to " + object; //$NON-NLS-1$
  }
  
  public final Object object;
  public final Member member;
  
  public BoundMember(Object object, Member member) throws NullPointerException {
    this.object = Objects.requireNonNull(object);
    this.member = Objects.requireNonNull(member);
  }
  
  public BoundMember(Object object, String path, Type type, Class<?>... argHints) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
    BoundMember bm = null;
    
    switch(type) {
      case ACCESSOR:
        bm = set(object, path, true, false, true, false, argHints);
        break;
        
      case MUTATOR:
        bm = set(object, path, true, false, false, true, argHints);
        break;
        
      case NEITHER:
        bm = set(object, path, true, false, false, false, argHints);
        break;
    }
    
    this.object = bm.object;
    this.member = bm.member;
  }
  
  private BoundMember set(Object object, String path, boolean allowAccessorsInChain, boolean allowMutatorsInChain, boolean allowAccessorsAtEnd, boolean allowMutatorsAtEnd, Class<?>... argHints) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
    String[] parts = path.split("\\."); //$NON-NLS-1$
    
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
      
      try {
        bm = new BoundMember(o, findMethodOrFieldByName(o.getClass(), snakeToCamel(part), accessors, mutators, argHints));
      } catch(NullPointerException e) {
        throw new NoSuchMethodException("Could not find the member or field \"" + part + "\" of \"" + path + "\" in object " + o + "."); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
      }
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