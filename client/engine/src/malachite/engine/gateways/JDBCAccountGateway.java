package malachite.engine.gateways;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import yesql.YeSQL;
import malachite.engine.exceptions.AccountException;
import malachite.engine.models.Character;
import malachite.engine.models.JDBCCharacter;
import malachite.engine.models.JDBCUser;
import malachite.engine.models.User;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.security.HasherInterface;
import malachite.validator.Validator;
import malachite.validator.Validator.ValidatorException;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private final GatewayProviderInterface _provider;
  private final HasherInterface _hasher;
  
  private final Method _prepareStatement;
  
  private final PreparedStatement _exists;
  private final PreparedStatement _login;
  private final PreparedStatement _register;
  private final PreparedStatement _chars;
  
  public JDBCAccountGateway(GatewayProviderInterface provider, HasherInterface hasher) {
    _provider = provider;
    _hasher = hasher;
    
    try {
      _prepareStatement = _provider.getClass().getMethod("prepareStatement", String.class, boolean.class); //$NON-NLS-1$
    } catch(NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }
    
    YeSQL yes = new YeSQL();
    
    _exists   = prepareStatement(yes.table(JDBCUser.TABLE).count ().where(JDBCUser.DB_EMAIL).equals().limit(1).build());
    _login    = prepareStatement(yes.table(JDBCUser.TABLE).select().where(JDBCUser.DB_EMAIL).equals().limit(1).build());
    _register = prepareStatement(yes.table(JDBCUser.TABLE).insert().value(JDBCUser.DB_EMAIL).value(JDBCUser.DB_PASSWORD).build(), true);
    
    _chars    = prepareStatement(yes.table(JDBCCharacter.TABLE).select().where(JDBCCharacter.DB_USER_ID).equals().build());
  }
  
  private PreparedStatement prepareStatement(String sql) {
    return prepareStatement(sql, false);
  }
  
  private PreparedStatement prepareStatement(String sql, boolean retreiveKeys) {
    try {
      return (PreparedStatement)_prepareStatement.invoke(_provider, sql, Boolean.valueOf(retreiveKeys));
    } catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override public User login(String email, String password) throws AccountException, ValidatorException, SQLException {
    new Validator()
      .check(email,    "email",    "email")
      .check(password, "password", "password");
    
    _login.setString(1, email);
    
    try(ResultSet r = _login.executeQuery()) {
      if(r.next()) {
        if(_hasher.check(password, r.getString(JDBCUser.DB_PASSWORD))) {
          return new JDBCUser(this, r);
        }
      }
    }
    
    throw new AccountException.InvalidLoginCredentials();
  }
  
  @Override public User register(String email, String password) throws AccountException, ValidatorException, SQLException {
    new Validator()
      .check(email,    "email",    "email")
      .check(password, "password", "password");
    
    _exists.setString(1, email);
    
    try(ResultSet r = _exists.executeQuery()) {
      r.next();
      
      if(r.getInt(1) != 0) {
        throw new AccountException.AccountAlreadyExists();
      }
    }
    
    _register.setString(1, email);
    _register.setString(2, _hasher.make(password));
    _register.executeUpdate();
    
    try(ResultSet r = _register.getGeneratedKeys()) {
      r.next();
      
      return new JDBCUser(this, r.getInt(1), email);
    }
  }
  
  @Override public Character[] getCharacters(User u) throws AccountException, SQLException {
    JDBCUser user = (JDBCUser)u;
    
    List<Character> chars = new ArrayList<>();
    
    _chars.setInt(1, user.id);
    
    try(ResultSet r = _chars.executeQuery()) {
      while(r.next()) {
        chars.add(new JDBCCharacter(this, user, r));
      }
    }
    
    Character[] c = new Character[chars.size()];
    return chars.toArray(c);
  }
}