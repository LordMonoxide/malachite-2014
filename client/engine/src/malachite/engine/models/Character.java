package malachite.engine.models;

import malachite.engine.gateways.AccountGatewayInterface;

public abstract class Character {
  private final AccountGatewayInterface _gateway;
  
  public final String name;
  
  public Character(AccountGatewayInterface gateway, String name) {
    _gateway = gateway;
    
    this.name = name;
  }
}