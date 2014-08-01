package malachite.gfx.util;

public final class Points {
  private Points() { }
  
  public static Point add(Point p1, Point p2) {
    return new Point(p1.getX() + p2.getX(), p1.getY() + p2.getY());
  }
  
  public static Point subtract(Point p1, Point p2) {
    return new Point(p1.getX() - p2.getX(), p1.getY() - p2.getY());
  }
  
  public static Point mean(Point p1, Point p2) {
    return new Point((p2.getX() - p1.getX()) / 2, (p2.getY() - p1.getY()) / 2);
  }
}