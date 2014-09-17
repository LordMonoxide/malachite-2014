package malachite.engine;

public final class Config {
  public final Lang lang = new Lang();
  public final DB   db   = new DB();
  
  public final class Lang {
    private Lang() { }
    
    public final String locale = "en-CA";
  }
  
  public final class DB {
    private DB() { }
    
    public final MySQL mysql = new MySQL();
    
    public final class MySQL {
      private MySQL() { }
      
      public final String host = "localhost";
      public final String database = "malachite";
      public final String username = "malachite";
      public final String password = "malachite";
    }
  }
  
  public final class Net {
    private Net() { }
    
    public final int port = 4000;
  }
}