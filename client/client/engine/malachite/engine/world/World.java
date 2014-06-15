package malachite.engine.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import malachite.engine.data.Map;

public class World implements Iterable<Entity> {
  @Override public String toString() {
    return "world '" + name + '\''; //$NON-NLS-1$
  }
  
  public final String name;

  private HashMap<String, Region> _region = new HashMap<>();
  private HashMap<String, Map>    _map    = new HashMap<>();
  
  private List<Entity> _entity = new ArrayList<>();
  
  public World(String name) {
    this.name = name;
  }
  
  public void add(Entity e) {
    _entity.add(e);
  }
  
  @Override public Iterator<Entity> iterator() {
    return _entity.iterator();
  }
  
  public Region getRegion(int x, int y) {
    String name = x + "x" + y; //$NON-NLS-1$
    Region r = _region.get(name);
    
    if(r == null) {
      Map m = _map.get(name);
      
      if(m == null) {
        m = new Map(x, y);
        //TODO:m.request();
        
        System.out.println("Map " + name + " requested."); //$NON-NLS-1$ //$NON-NLS-2$
        
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