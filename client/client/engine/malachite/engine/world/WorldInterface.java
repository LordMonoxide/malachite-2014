package malachite.engine.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import malachite.engine.Engine;
import malachite.engine.data.MapInterface;

public abstract class WorldInterface {
  @Override public String toString() {
    return "world '" + name + '\''; //$NON-NLS-1$
  }
  
  public final String name;

  private HashMap<String, Region>       _region = new HashMap<>();
  private HashMap<String, MapInterface> _map    = new HashMap<>();
  
  private List<Entity> _entity = new ArrayList<>();
  
  public WorldInterface(String name) {
    this.name = name;
  }
  
  public void add(Entity e) {
    _entity.add(e);
  }
  
  public Region getRegion(int x, int y) {
    String name = x + "x" + y; //$NON-NLS-1$
    Region r = _region.get(name);
    
    if(r == null) {
      MapInterface m = _map.get(name);
      
      if(m == null) {
        System.out.println("Map " + name + " requested."); //$NON-NLS-1$ //$NON-NLS-2$
        
        m = Engine.newMap(x, y);
        m.load(map -> {
          System.out.println("Map " + name + " loaded."); //$NON-NLS-1$ //$NON-NLS-2$
        });
        
        _map.put(name, m);
      }
      
      r = new Region(this, m);
      _region.put(name, r);
    }
    
    return r;
  }
  
  public static interface Listener {
    
  }
}