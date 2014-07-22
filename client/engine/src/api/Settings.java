package api;

public class Settings {
  public static class Map {
    public static final int Size  = Settings.Map.Tile.Size * Settings.Map.Tile.Count;
    public static final int Depth = 7;
    
    public static class Tile {
      public static final int Size  = 32;
      public static final int Count = 32;
    }
    
    public static class Attrib {
      public static final int Resolution = 2;
      public static final int Size  = Settings.Map.Tile.Size  / Resolution;
      public static final int Count = Settings.Map.Tile.Count * Resolution;
    }
  }
}
