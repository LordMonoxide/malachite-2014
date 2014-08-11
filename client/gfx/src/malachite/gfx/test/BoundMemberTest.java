package malachite.gfx.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import malachite.gfx.util.BoundMember;
import malachite.gfx.util.ReflectionUtils;
import static org.junit.Assert.*;

import org.junit.Test;

public class BoundMemberTest {
  @Test(expected = NullPointerException.class)
  public void nullObjectShouldCauseNPE() {
    new BoundMember(null, this.getClass().getDeclaredMethods()[0]);
  }

  @Test(expected = NullPointerException.class)
  public void nullMemberShouldCauseNPE() {
    new BoundMember(new Object(), null);
  }
  
  @Test
  public void basicConstructor() {
    SimpleObject object = new SimpleObject();
    Method methodGet = ReflectionUtils.findMethodByName(object.getClass(), "getInt");
    Method methodSet = ReflectionUtils.findMethodByName(object.getClass(), "setInt");
    
    assertEquals("getInt was actually " + methodGet, "getInt", methodGet.getName());
    assertEquals("setInt was actually " + methodSet, "setInt", methodSet.getName());
    
    methodGet.setAccessible(true);
    methodSet.setAccessible(true);
    
    BoundMember get = new BoundMember(object, methodGet);
    BoundMember set = new BoundMember(object, methodSet);
    
    assertEquals("Object gets are inequal", object, get.object);
    assertEquals("Object sets are inequal", object, set.object);
    assertEquals("Gets are inequal", methodGet, get.member);
    assertEquals("Sets are inequal", methodSet, set.member);
    
    try {
      set.setValue(1);
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail("Set failed");
    } catch(IllegalArgumentException e) {
      fail("Invalid arguments for " + set);
    }
    
    try {
      assertEquals("Value should have been 1", 1, get.getValue());
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail("Get failed");
    } catch(IllegalArgumentException e) {
      fail("Invalid arguments for " + get);
    }
  }
  
  @Test
  public void pathConstructor() {
    SimpleObject object = new SimpleObject();
    BoundMember get = null;
    BoundMember set = null;
    
    try {
      get = new BoundMember(object, "int", BoundMember.Type.ACCESSOR);
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access in get " + get);
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An error was thrown in a path accessor in get " + get);
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor of get " + get);
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method in get " + get);
    }
    
    assertNotNull(get);
    
    try {
      set = new BoundMember(object, "int", BoundMember.Type.MUTATOR);
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access in set " + set);
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor in set " + set);
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor of set " + set);
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method in set " + set);
    }
    
    assertNotNull(set);
    
    try {
      set.setValue(1);
      assertEquals("Value should have been 1", 1, get.getValue());
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while setting value");
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("Illegal argument while setting value");
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in value setter");
    }
  }
  
  @Test
  public void nestedPathConstructor() {
    SimpleObject object = new SimpleObject();
    BoundMember list = null;
    
    try {
      list = new BoundMember(object, "list.size", BoundMember.Type.NEITHER);
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while finding member");
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor while finding member");
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor while finding member");
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method while finding member");
    }
    
    assertNotNull(list);
    
    try {
      assertEquals("List length should have been 0", 0, list.getValue());
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while getting value");
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor while getting value");
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor while getting value");
    }
  }
  
  private class SimpleObject {
    public List<Object> list = new ArrayList<>();
    
    public boolean testField;
    
    private int    _testInt;
    private String _testStr;
    
    public int    getInt() { return _testInt; }
    public String getStr() { return _testStr; }
    
    public void setInt(int    testInt) { _testInt = testInt; }
    public void setStr(String testStr) { _testStr = testStr; }
  }
}