package malachite.gfx.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import malachite.gfx.util.BoundMember;
import malachite.gfx.util.ReflectionUtils;

import org.junit.Assert;
import org.junit.Test;

public class BoundMemberTest {
  @Test
  public void basicConstructor() {
    SimpleObject object = new SimpleObject();
    Method methodGet = ReflectionUtils.findMethodByName(object.getClass(), "getInt");
    Method methodSet = ReflectionUtils.findMethodByName(object.getClass(), "setInt");
    
    Assert.assertEquals("getInt was actually " + methodGet, "getInt", methodGet.getName());
    Assert.assertEquals("setInt was actually " + methodSet, "setInt", methodSet.getName());
    
    methodGet.setAccessible(true);
    methodSet.setAccessible(true);
    
    BoundMember get = new BoundMember(object, methodGet);
    BoundMember set = new BoundMember(object, methodSet);
    
    Assert.assertEquals("Object gets are inequal", object, get.object);
    Assert.assertEquals("Object sets are inequal", object, set.object);
    Assert.assertEquals("Gets are inequal", methodGet, get.member);
    Assert.assertEquals("Sets are inequal", methodSet, set.member);
    
    try {
      set.setValue(1);
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      Assert.fail("Set failed");
    } catch(IllegalArgumentException e) {
      Assert.fail("Invalid arguments for " + set);
    }
    
    try {
      Assert.assertEquals("Value should have been 1", 1, get.getValue());
    } catch(IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
      Assert.fail("Get failed");
    } catch(IllegalArgumentException e) {
      Assert.fail("Invalid arguments for " + get);
    }
  }
  
  @Test
  public void pathConstructor() {
    SimpleObject object = new SimpleObject();
    
    try {
      BoundMember get = new BoundMember(object, "int", BoundMember.Type.ACCESSOR);
      BoundMember set = new BoundMember(object, "int", BoundMember.Type.MUTATOR);
      
      set.setValue(1);
      Assert.assertEquals("Value should have been 1", 1, get.getValue());
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }
  
  @Test
  public void nestedPathConstructor() {
    SimpleObject object = new SimpleObject();
    
    try {
      BoundMember list = new BoundMember(object, "list.size", BoundMember.Type.NEITHER);
      Assert.assertEquals("List length should have been 0", 0, list.getValue());
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
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