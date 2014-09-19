package malachite.game.data;

public class Map implements LoadableInterface {
  public final String id;
  public final int x, y;
  
  public final Layer[] layer = new Layer[5];
  
  public Map(int regionX, int regionY) {
    id = regionX + "," + regionY;
    x  = regionX;
    y  = regionY;
    
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
  
  public String type() {
    return "map";
  }
  
  public String id() {
    return id;
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