package malachite.engine.world.entity;

import malachite.engine.util.Point;

public abstract class Entity {
  public final int id;
  
  private Point _loc = new Point();
  
  public Entity(int id) {
    this.id = id;
  }
  
  public Point loc() { return _loc; }
}