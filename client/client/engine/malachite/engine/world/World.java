package malachite.engine.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import malachite.engine.world.entity.Entity;

public class World implements Iterable<Entity> {
  private List<Entity> _entity = new ArrayList<>();
  
  public void add(Entity e) {
    _entity.add(e);
  }
  
  @Override public Iterator<Entity> iterator() {
    return _entity.iterator();
  }
  
  public static interface Listener {
    
  }
}