package malachite.engine.jdbc;

import malachite.engine.Config;

public class JDBCInitializer implements JDBCInitializerInterface {
  public JDBCInitializer(Config.DB config) {
    throw new RuntimeException("JDBCInitializer was not overridden!");
  }
  
  @Override public String buildConnectionString() {
    return null;
  }
}