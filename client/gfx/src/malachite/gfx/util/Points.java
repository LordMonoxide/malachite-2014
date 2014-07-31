package malachite.gfx.util;

public final class Points {
  private Points() { }
  
  public static Point add(Point p1, Point p2) {
    return new Point(p1._x + p2._x, p1._y + p2._y);
  }
  
  public static Point subtract(Point p1, Point p2) {
    return new Point(p1._x - p2._x, p1._y - p2._y);
  }
  
  public static Point mean(Point p1, Point p2) {
    return new Point((p2._x - p1._x) / 2, (p2._y - p1._y) / 2);
  }
}