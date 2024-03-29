package malachite.engine.providers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

import malachite.engine.Engine;
import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.gateways.JDBCAccountGateway;
import malachite.engine.jdbc.JDBCInitializer;

public class JDBCGatewayProvider implements GatewayProviderInterface {
  @Override public String toString() {
    return "JDBCGatewayProvider (" + super.toString() + ')'; //$NON-NLS-1$
  }
  
  private AccountGatewayInterface _account;
  private Engine _engine;
  
  private BoneCP _pool;
  
  private List<PreparedStatement> _statements = new ArrayList<>();
  
  public JDBCGatewayProvider(Engine engine) throws SQLException {
    _engine = engine;
    
    BoneCPConfig config = new BoneCPConfig();
    config.setJdbcUrl(new JDBCInitializer(engine.config.db).buildConnectionString());
    config.setPartitionCount(4);
    
    _pool = new BoneCP(config);
  }
  
  @Override public AccountGatewayInterface accountGateway() {
    if(_account == null) { _account = new JDBCAccountGateway(this, _engine.providers.security.hasher()); }
    
    return _account;
  }
  
  public Connection getConnection() throws SQLException {
    return _pool.getConnection();
  }
  
  public PreparedStatement prepareStatement(String sql) throws SQLException {
    return prepareStatement(sql, false);
  }
  
  public PreparedStatement prepareStatement(String sql, boolean retreiveID) throws SQLException {
    PreparedStatement statement = getConnection().prepareStatement(sql, retreiveID ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS);
    _statements.add(statement);
    return statement;
  }
}