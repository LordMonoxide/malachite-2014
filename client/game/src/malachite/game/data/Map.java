package malachite.game.data;

public class Map {
  public final Layer[] layer = new Layer[5];
  
  public Map() {
    for(int i = 0; i < layer.length; i++) {
      layer[i] = new Layer();
    }
    
    for(int x = 0; x < 16; x++) {
      for(int y = 0; y < 16; y++) {
        Tile tile = layer[0].tile[x][y];
        tile.alpha = (byte)255;
      }
    }
  }
  
  public class Layer {
    public final Tile[][] tile = new Tile[16][16];
    
    public Layer() {
      for(int x = 0; x < tile.length; x++) {
        for(int y = 0; y < tile.length; y++) {
          tile[x][y] = new Tile();
        }
      }
    }
  }
  
  public class Tile {
    public byte alpha;
    public byte x, y;
    public byte set;
  }
}