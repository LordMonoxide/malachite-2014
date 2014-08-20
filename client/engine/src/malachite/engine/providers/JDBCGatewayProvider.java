package malachite.engine.providers;

import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.gateways.JDBCAccountGateway;

public class JDBCGatewayProvider implements GatewayProviderInterface {
  private AccountGatewayInterface _account;
  
  private BoneCP _pool;
  
  public JDBCGatewayProvider(JDBCInitializer initializer) throws SQLException {
    BoneCPConfig config = new BoneCPConfig();
    config.setJdbcUrl(initializer.buildConnectionString());
    config.setPartitionCount(4);
    
    _pool = new BoneCP(config);
  }
  
  @Override public AccountGatewayInterface accountGateway() {
    if(_account == null) { _account = new JDBCAccountGateway(this); }
    return _account;
  }
  
  public interface JDBCInitializer {
    String buildConnectionString();
  }
  
  public static class MySQLInitializer implements JDBCInitializer {
    static {
      try {
        Class.forName("com.mysql.jdbc.Driver"); //$NON-NLS-1$
      } catch(ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
    }
    
    private final String _host, _database, _username, _password;
    
    public MySQLInitializer(String host, String database, String username, String password) {
      _host     = host;
      _database = database;
      _username = username;
      _password = password;
    }
    
    @Override public String buildConnectionString() {
      return "jdbc:mysql://" + _host + '/' + _database + "?user=" + _username + "&password=" + _password; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }
  }
}