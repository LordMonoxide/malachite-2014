package malachite.engine.gateways;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import yesql.YeSQL;
import malachite.engine.exceptions.AccountException;
import malachite.engine.models.User;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.security.HasherInterface;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private final GatewayProviderInterface _provider;
  private final HasherInterface _hasher;
  
  private final Method _prepareStatement;
  
  private final PreparedStatement _exists;
  private final PreparedStatement _login;
  private final PreparedStatement _register;
  
  public JDBCAccountGateway(GatewayProviderInterface provider, HasherInterface hasher) {
    _provider = provider;
    _hasher = hasher;
    
    try {
      _prepareStatement = _provider.getClass().getMethod("prepareStatement", String.class, boolean.class); //$NON-NLS-1$
    } catch(NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
    
    YeSQL yes = new YeSQL();
    
    _exists   = prepareStatement(yes.table(User.TABLE).count ().where(User.DB_EMAIL).equals().limit(1).build(), false);
    _login    = prepareStatement(yes.table(User.TABLE).select().where(User.DB_EMAIL).equals().limit(1).build(), false);
    _register = prepareStatement(yes.table(User.TABLE).insert().value(User.DB_EMAIL).value(User.DB_PASSWORD).build(), true);
  }
  
  private PreparedStatement prepareStatement(String sql, boolean retreiveKeys) {
    try {
      return (PreparedStatement)_prepareStatement.invoke(_provider, sql, retreiveKeys);
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
  
  @Override public User register(String email, String password) throws AccountException, SQLException {
    _exists.setString(1, email);
    
    try(ResultSet r = _exists.executeQuery()) {
      r.next();
      
      if(r.getInt(1) != 0) {
        throw new AccountException.AccountAlreadyExists();
      }
    }
    
    _register.setString(1, email);
    _register.setString(2, password);
    //TODO: Execute, get keys
    
    return null;
  }
  
  private User userFromResultSet(ResultSet r) throws SQLException {
    return new User(r.getString(User.DB_EMAIL));
  }
}