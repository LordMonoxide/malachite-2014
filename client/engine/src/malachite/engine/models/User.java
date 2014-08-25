package malachite.engine.models;

import malachite.engine.exceptions.AccountException;
import malachite.engine.gateways.AccountGatewayInterface;

public abstract class User<T> {
  public static final String TABLE       = "users";    //$NON-NLS-1$
  public static final String DB_ID       = "id";       //$NON-NLS-1$
  public static final String DB_EMAIL    = "email";    //$NON-NLS-1$
  public static final String DB_PASSWORD = "password"; //$NON-NLS-1$
  
  private AccountGatewayInterface _gateway;
  
  private T _id;
  private Character[] _chars;
  
  public final String email;
  
  public User(AccountGatewayInterface gateway, T id, String email) {
    _gateway = gateway;
    _id      = id;
    
    this.email = email;
  }
  
  public Character[] characters() throws AccountException, Exception {
    if(_chars == null) {
      _chars = _gateway.getCharacters(this);
    }
    
    return _chars;
  }
}