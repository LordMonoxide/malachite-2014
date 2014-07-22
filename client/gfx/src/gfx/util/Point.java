package gfx.util;

public class Point {
  @Override public String toString() {
    return "(" + _x + ", " + _y + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  protected float _x, _y;
  protected Point _bind;
  
  public Point() { }
  
  public Point(float x, float y, Point bind) {
    this(x, y);
    _bind = bind;
  }
  
  public Point(float x, float y) {
    _x = x;
    _y = y;
  }
  
  public Point(Point p) {
    clone(p);
  }
  
  public void set(float x, float y) { _x = x; _y = y; }
  public void setX(float x) { _x = x; }
  public void setY(float y) { _y = y; }
  public float getX() { return _x + (_bind != null ? _bind.getX() : 0); }
  public float getY() { return _y + (_bind != null ? _bind.getY() : 0); }
  
  public void clone(Point p) {
    _x    = p._x;
    _y    = p._y;
    _bind = p._bind;
  }
  
  public void bind(Point p) {
    _bind = p;
  }
}