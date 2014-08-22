package malachite.engine.gateways;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.exceptions.AccountException;
import malachite.engine.models.User;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.security.HasherInterface;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private final GatewayProviderInterface _provider;
  private final HasherInterface _hasher;
  
  private final Method _prepareStatement;
  
  private final PreparedStatement _login;
  
  public JDBCAccountGateway(GatewayProviderInterface provider, HasherInterface hasher) {
    _provider = provider;
    _hasher = hasher;
    
    try {
      _prepareStatement = _provider.getClass().getMethod("prepareStatement", String.class); //$NON-NLS-1$
    } catch(NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
    
    _login = prepareStatement("SELECT * FROM users WHERE email=? LIMIT 1"); //$NON-NLS-1$
  }
  
  private PreparedStatement prepareStatement(String sql) {
    try {
      return (PreparedStatement)_prepareStatement.invoke(_provider, sql);
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override public User login(String email, String password) throws AccountException, SQLException {
    _login.setString(1, email);
    
    try(ResultSet r = _login.executeQuery()) {
      if(r.next()) {
        if(_hasher.check(password, r.getString(User.DB_PASSWORD))) {
          return userFromResultSet(r);
        }
      }
    }
    
    throw new AccountException.InvalidLoginCredentials();
  }
  
  @Override public User register(String email, String password, String passwordConfirmation) throws AccountException, SQLException {
    return null;
  }
  
  private User userFromResultSet(ResultSet r) throws SQLException {
    return new User(r.getString(User.DB_EMAIL));
  }
}