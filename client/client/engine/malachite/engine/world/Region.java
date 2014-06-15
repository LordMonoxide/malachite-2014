package malachite.engine.world;

import malachite.api.Settings;
import malachite.engine.data.Map;

public class Region {
  public final World world;
  public final Map map;
  public final int x, y;
  public final String name;
  
  public Region(World world, Map map) {
    this.world = world;
    this.map = map;
    this.x = map.x * Settings.Map.Size;
    this.y = map.x * Settings.Map.Size;
    name = x + "x" + y; //$NON-NLS-1$
  }
}