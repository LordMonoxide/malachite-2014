package api;

import api.gateways.HTTPAuthGateway;
import api.gateways.HTTPLangGateway;
import api.gateways.IAuthGateway;
import api.gateways.ILangGateway;
import api.repositories.HTTPUserRepository;
import api.repositories.IUserRepository;

public final class Instances {
  public static IUserRepository newUserRepository() {
    return new HTTPUserRepository();
  }
  
  public static ILangGateway newLangGateway() {
    return new HTTPLangGateway();
  }
  
  public static IAuthGateway newAuthGateway() {
    return new HTTPAuthGateway();
  }
  
  private Instances() { }
}