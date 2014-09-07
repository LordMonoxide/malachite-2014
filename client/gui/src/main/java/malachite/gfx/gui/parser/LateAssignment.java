package malachite.gfx.gui.parser;

import java.lang.reflect.InvocationTargetException;

import malachite.gfx.util.BoundMember;

public class LateAssignment {
  private BoundMember   _bm;
  private BoundMember[] _value;
  
  public LateAssignment(BoundMember bm, BoundMember... value) {
    _bm    = bm;
    _value = value;
  }
  
  public void assign() throws ParserException.LateAssignmentException {
    Object[] values = new Object[_value.length];
    
    int i = 0;
    for(BoundMember value : _value) {
      try {
        values[i++] = value.getValue();
      } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new ParserException.ErrorGettingValueException(value, e);
      }
    }
    
    try {
      _bm.setValue((Object[])values);
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new ParserException.ErrorSettingValueException(_bm, e);
    }
  }
}