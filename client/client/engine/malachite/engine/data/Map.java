package malachite.engine.data;

import malachite.api.Settings;

public class Map {
  public final int x, y;
  public final Layer[] layer = new Layer[Settings.Map.Depth];
  
  public Map(int x, int y) {
    this.x = x;
    this.y = y;
    
    for(int z = 0; z < Settings.Map.Depth; z++) {
      layer[z] = new Layer();
    }
    
    for(int x1 = 0; x1 < Settings.Map.Tile.Count; x1++) {
      for(int y1 = 0; y1 < Settings.Map.Tile.Count; y1++) {
        layer[0].tile[x1][y1].a = (byte)255;
      }
    }
  }
  
  public class Layer {
    public final Tile  [][] tile   = new Tile  [Settings.Map.Tile  .Count][Settings.Map.Tile  .Count];
    public final Attrib[][] attrib = new Attrib[Settings.Map.Attrib.Count][Settings.Map.Attrib.Count];
    
    private Layer() {
      for(int x = 0; x < tile.length; x++) {
        for(int y = 0; y < tile[x].length; y++) {
          tile[x][y] = new Tile();
        }
      }
      
      for(int x = 0; x < attrib.length; x++) {
        for(int y = 0; y < attrib[x].length; y++) {
          attrib[x][y] = new Attrib();
        }
      }
    }
  }
  
  public class Tile {
    public byte x, y;
    public byte tileset;
    public byte a;
  }
  
  public static class Attrib {
    public Type type;
    
    public static enum Type {
      BLOCKED((byte)0x80, new byte[] {(byte)255, 0, 0, (byte)255});
      
      public static Type fromVal(int val) {
        for(Type t : Type.values()) {
          if(t._val == val) {
            return t;
          }
        }
        
        return null;
      }
      
      private final byte   _val;
      private final byte[] _col;
      
      private Type(byte val, byte[] col) {
        _val = val;
        _col = col;
      }
      
      public int val() {
        return _val;
      }
      
      public byte[] col() {
        return _col;
      }
    }
  }
}