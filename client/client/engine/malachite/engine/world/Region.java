package malachite.engine.world;

import malachite.api.Settings;
import malachite.engine.data.MapInterface;

public class Region {
  public final WorldInterface world;
  public final MapInterface map;
  public final int x, y;
  public final String name;
  
  public Region(WorldInterface world, MapInterface map) {
    this.world = world;
    this.map = map;
    this.x = map.x * Settings.Map.Size;
    this.y = map.x * Settings.Map.Size;
    name = x + "x" + y; //$NON-NLS-1$
  }
}