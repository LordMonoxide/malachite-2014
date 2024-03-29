package malachite.gfx.gui;

public class ControlList {
  private Control<? extends ControlEvents> _parent;
  private Control<? extends ControlEvents> _first;
  private Control<? extends ControlEvents> _last;
  private int _size;

  protected ControlList(Control<? extends ControlEvents> parent) {
    _parent = parent;
  }

  public int size() {
    return _size;
  }

  public void add(Control<? extends ControlEvents> control) {
    control._controlParent = _parent;
    control.setGUI(_parent._gui);

    if(_first != null) {
      control._controlNext = null;
      control._controlPrev = _first;
      _first._controlNext = control;
      _first = control;
    } else {
      control._controlNext = null;
      control._controlPrev = null;
      _first = control;
      _last  = control;
    }

    _size++;
  }

  public void remove(Control<? extends ControlEvents> control) {
    Control<? extends ControlEvents> c = control._controlNext;
    if(c != null) {
      c._controlPrev = control._controlPrev;

      if(c._controlPrev == null) {
        _last = c;
      }
    } else {
      c = control._controlPrev;
      if(c != null) {
        c._controlNext = null;
      }

      _first = c;
    }

    c = control._controlPrev;
    if(c != null) {
      c._controlNext = control._controlNext;

      if(c._controlNext == null) {
        _first = c;
      }
    } else {
      c = control._controlNext;

      if(c != null) {
        c._controlPrev = null;
      }

      _last = c;
    }

    _size--;
  }

  void killFocus() {
    Control<? extends ControlEvents> c = _last;

    while(c != null) {
      c.setFocus(false);
      c._controlList.killFocus();
      c = c._controlNext;
    }
  }
  
  void enable() {
    Control<? extends ControlEvents> c = _last;
    
    while(c != null) {
      c.enable();
      c = c._controlNext;
    }
  }
  
  void disable() {
    Control<? extends ControlEvents> c = _last;
    
    while(c != null) {
      c.disable();
      c = c._controlNext;
    }
  }

  Control<? extends ControlEvents> first() {
    return _first;
  }

  Control<? extends ControlEvents> last() {
    return _last;
  }

  void draw() {
    if(_last != null) {
      _last.draw();
    }
  }

  void logic() {
    if(_last != null) {
      _last.logicControl();
    }
  }

  void drawSelect() {
    if(_last != null) {
      _last.drawSelect();
    }
  }

  Control<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_last != null) {
      return _last.getSelectControl(colour);
    }
    
    return null;
  }
}