package malachite.engine.jdbc;

import malachite.engine.Config;

public class MySQLInitializer implements JDBCInitializerInterface {
  static {
    try {
      Class.forName("com.mysql.jdbc.Driver"); //$NON-NLS-1$
    } catch(ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  private final String _host, _database, _username, _password;
  
  public MySQLInitializer(Config.DB config) {
    _host     = config.mysql.host;
    _database = config.mysql.database;
    _username = config.mysql.username;
    _password = config.mysql.password;
  }
  
  @Override public String buildConnectionString() {
    return "jdbc:mysql://" + _host + '/' + _database + "?user=" + _username + "&password=" + _password; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
}