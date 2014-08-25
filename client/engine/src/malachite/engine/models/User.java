package malachite.engine.models;

import malachite.engine.exceptions.AccountException;
import malachite.engine.gateways.AccountGatewayInterface;

public abstract class User {
  private final AccountGatewayInterface _gateway;
  
  private Character[] _chars;
  
  public final String email;
  
  public User(AccountGatewayInterface gateway, String email) {
    _gateway = gateway;
    
    this.email = email;
  }
  
  public Character[] characters() throws AccountException, Exception {
    if(_chars == null) {
      _chars = _gateway.getCharacters(this);
    }
    
    return _chars;
  }
}