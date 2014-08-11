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
    Method methodGet = ReflectionUtils.findMethodByName(object.getClass(), "getInt"); //$NON-NLS-1$
    Method methodSet = ReflectionUtils.findMethodByName(object.getClass(), "setInt"); //$NON-NLS-1$
    
    assertEquals("getInt was actually " + methodGet, "getInt", methodGet.getName()); //$NON-NLS-1$ //$NON-NLS-2$
    assertEquals("setInt was actually " + methodSet, "setInt", methodSet.getName()); //$NON-NLS-1$ //$NON-NLS-2$
    
    methodGet.setAccessible(true);
    methodSet.setAccessible(true);
    
    BoundMember get = new BoundMember(object, methodGet);
    BoundMember set = new BoundMember(object, methodSet);
    
    assertEquals("Object gets are inequal", object, get.object); //$NON-NLS-1$
    assertEquals("Object sets are inequal", object, set.object); //$NON-NLS-1$
    assertEquals("Gets are inequal", methodGet, get.member); //$NON-NLS-1$
    assertEquals("Sets are inequal", methodSet, set.member); //$NON-NLS-1$
    
    try {
      set.setValue(Integer.valueOf(1));
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail("Set failed"); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      fail("Invalid arguments for " + set); //$NON-NLS-1$
    }
    
    try {
      assertEquals("Value should have been 1", Integer.valueOf(1), get.getValue()); //$NON-NLS-1$
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      fail("Get failed"); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      fail("Invalid arguments for " + get); //$NON-NLS-1$
    }
  }
  
  @Test
  public void pathConstructor() {
    SimpleObject object = new SimpleObject();
    BoundMember get = null;
    BoundMember set = null;
    
    try {
      get = new BoundMember(object, "int", BoundMember.Type.ACCESSOR); //$NON-NLS-1$
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access in get " + get); //$NON-NLS-1$
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An error was thrown in a path accessor in get " + get); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor of get " + get); //$NON-NLS-1$
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method in get " + get); //$NON-NLS-1$
    }
    
    assertNotNull(get);
    
    try {
      set = new BoundMember(object, "int", BoundMember.Type.MUTATOR); //$NON-NLS-1$
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access in set " + set); //$NON-NLS-1$
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor in set " + set); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor of set " + set); //$NON-NLS-1$
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method in set " + set); //$NON-NLS-1$
    }
    
    assertNotNull(set);
    
    try {
      set.setValue(Integer.valueOf(1));
      assertEquals("Value should have been 1", Integer.valueOf(1), get.getValue()); //$NON-NLS-1$
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while setting value"); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("Illegal argument while setting value"); //$NON-NLS-1$
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in value setter"); //$NON-NLS-1$
    }
  }
  
  @Test
  public void nestedPathConstructor() {
    SimpleObject object = new SimpleObject();
    BoundMember list = null;
    
    try {
      list = new BoundMember(object, "list.size", BoundMember.Type.NEITHER); //$NON-NLS-1$
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while finding member"); //$NON-NLS-1$
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor while finding member"); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor while finding member"); //$NON-NLS-1$
    } catch(NoSuchMethodException e) {
      e.printStackTrace();
      fail("There is no such method while finding member"); //$NON-NLS-1$
    }
    
    assertNotNull(list);
    
    try {
      assertEquals("List length should have been 0", new Integer(0), list.getValue()); //$NON-NLS-1$
    } catch(IllegalAccessException e) {
      e.printStackTrace();
      fail("Illegal access while getting value"); //$NON-NLS-1$
    } catch(InvocationTargetException e) {
      e.printStackTrace();
      fail("An exception was thrown in a path accessor while getting value"); //$NON-NLS-1$
    } catch(IllegalArgumentException e) {
      e.printStackTrace();
      fail("An illegal argument was passed to an accessor while getting value"); //$NON-NLS-1$
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