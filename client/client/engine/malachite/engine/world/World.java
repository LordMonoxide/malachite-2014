package malachite.engine.world;

import java.util.ArrayList;
import java.util.List;

import malachite.engine.world.entity.Entity;

public class World {
  private List<Entity> _entity = new ArrayList<>();
  
  public void add(Entity e) {
    _entity.add(e);
  }
}