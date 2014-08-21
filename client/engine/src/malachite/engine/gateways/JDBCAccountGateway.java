package malachite.engine.gateways;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.exceptions.AccountException;
import malachite.engine.models.User;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.providers.JDBCGatewayProvider;
import malachite.engine.security.HasherInterface;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private GatewayProviderInterface _provider;
  private HasherInterface _hasher;
  
  private PreparedStatement _login;
  
  public JDBCAccountGateway(GatewayProviderInterface provider, HasherInterface hasher) throws SQLException {
    _provider = provider;
    _hasher = hasher;
    
    //_login = _provider.prepareStatement("SELECT * FROM users WHERE email=? LIMIT 1"); //$NON-NLS-1$
  }
  
  @Override public User login(String email, String password) throws AccountException, SQLException {
    _login.setString(1, email);
    
    ResultSet r = _login.executeQuery();
    if(r.next()) {
      if(_hasher.check(password, r.getString("password"))) {
        
      } else {
        throw new AccountException.InvalidLoginCredentials();
      }
    } else {
      //error
    }
    
    return null;
  }
  
  private User userFromResultSet(ResultSet r) {
    return null;
  }
}