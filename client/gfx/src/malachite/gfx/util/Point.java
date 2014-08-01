package malachite.gfx.util;

import java.util.HashSet;
import java.util.Set;

public class Point {
  @Override public String toString() {
    return "(" + getX() + ", " + getY() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  protected float _x, _y;
  protected Point _bindX;
  protected Point _bindY;
  
  private Set<Point> _children = new HashSet<>();
  
  public Point() { }
  
  public Point(float x, float y, Point bind) {
    this(x, y);
    bind(bind);
  }
  
  public Point(float x, float y) {
    set(x, y);
  }
  
  public Point(Point p) {
    clone(p);
  }
  
  public void set(float x, float y) { _x = x; _y = y; propogate(); }
  public void set(Point p) { set(p.getX(), p.getY()); }
  public void setX(float x) { set(x, _y); }
  public void setY(float y) { set(_x, y); }
  public float getX() { return _x + (_bindX != null ? _bindX.getX() : 0); }
  public float getY() { return _y + (_bindY != null ? _bindY.getY() : 0); }
  
  public void clone(Point p) {
    bind(p._bindX, p._bindY);
    set(p._x, p._y);
  }
  
  public void bind(Point p) {
    bind(p, p);
  }
  
  public void bind(Point x, Point y) {
    bindX(x);
    bindY(y);
  }
  
  public void bindX(Point p) {
    if(p != null) {
      p._children.add(this);
    } else {
      if(_bindX != null) {
        _bindX._children.remove(this);
      }
    }
    
    _bindX = p;
    propogate();
  }
  
  public void bindY(Point p) {
    if(p != null) {
      p._children.add(this);
    } else {
      if(_bindX != null) {
        _bindX._children.remove(this);
      }
    }
    
    _bindY = p;
    propogate();
  }
  
  private boolean _propogating = false;
  
  private void propogate() {
    if(_propogating) { return; }
    _propogating = true;
    
    set(_x, _y);
    
    for(Point p : _children) {
      p.set(p._x, p._y);
    }
    
    _propogating = false;
  }
}