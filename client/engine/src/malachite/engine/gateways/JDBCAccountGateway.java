package malachite.engine.gateways;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.providers.JDBCGatewayProvider;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private JDBCGatewayProvider _provider;
  
  private PreparedStatement _login;
  
  public JDBCAccountGateway(JDBCGatewayProvider provider) throws SQLException {
    _provider = provider;
    
    _login = _provider.prepareStatement("SELECT * FROM users WHERE email=? LIMIT 1"); //$NON-NLS-1$
  }
  
  @Override public void login(String email, String password) throws SQLException {
    _login.setString(1, email);
    
    ResultSet r = _login.executeQuery();
    if(r.next()) {
      
    } else {
      //error
    }
  }
}