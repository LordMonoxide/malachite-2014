package gfx.util;

import java.util.HashSet;
import java.util.Set;

public class Point {
  @Override public String toString() {
    return "(" + _x + ", " + _y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
  public void setX(float x) { _x = x; propogate(); }
  public void setY(float y) { _y = y; propogate(); }
  public float getX() { return _x + (_bindX != null ? _bindX.getX() : 0); }
  public float getY() { return _y + (_bindY != null ? _bindY.getY() : 0); }
  
  public void clone(Point p) {
    set(p._x, p._y);
    bind(p._bindX, p._bindY);
  }
  
  public void bind(Point p) {
    bind(p, p);
  }
  
  public void bind(Point x, Point y) {
    bindX(x);
    bindY(y);
  }
  
  public void bindX(Point p) {
    _bindX = p;
    
    if(p != null) {
      _children.add(p);
    } else {
      _children.remove(p);
    }
  }
  
  public void bindY(Point p) {
    _bindY = p;
    
    if(p != null) {
      _children.add(p);
    } else {
      _children.remove(p);
    }
  }
  
  private void propogate() {
    for(Point p : _children) {
      p.set(p._x, p._y);
      p.propogate();
    }
  }
}