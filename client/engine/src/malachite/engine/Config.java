package malachite.engine;

public final class Config {
  public final DB db = new DB();
  
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
}