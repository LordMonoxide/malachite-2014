package malachite.server.data;

import malachite.api.Settings;
import malachite.engine.data.MapInterface;

public class Map extends MapInterface {
  public Map(int x, int y) {
    super(x, y);
  }
  
  @Override public void loadImpl() {
    for(int x1 = 0; x1 < Settings.Map.Tile.Count; x1++) {
      for(int y1 = 0; y1 < Settings.Map.Tile.Count; y1++) {
        layer[0].tile[x1][y1].a = (byte)255;
      }
    }
  }
}