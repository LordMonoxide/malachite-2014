package malachite.engine.models;

import malachite.engine.gateways.AccountGatewayInterface;

public abstract class Character {
  private final AccountGatewayInterface _gateway;
  
  public final User   user;
  public final String name;
  
  public Character(AccountGatewayInterface gateway, User user, String name) {
    _gateway = gateway;
    
    this.user = user;
    this.name = name;
  }
  
  public void remove() throws Exception {
    user.characters.remove(this);
  }
}