package malachite.game.world;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class World {
  private final Map<Integer, Entity> _entities = new HashMap<>();
  
  public final Entities entities = new Entities();
  
  public void draw() {
    for(Entity entity : entities) {
      entity.draw();
    }
  }
  
  public class Entities implements Iterable<Entity> {
    public void add(Entity entity) {
      _entities.put(Integer.valueOf(entity.id), entity);
    }
    
    @Override public Iterator<Entity> iterator() {
      return _entities.values().iterator();
    }
  }
}