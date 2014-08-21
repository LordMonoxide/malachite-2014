package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.gateways.AccountGatewayInterface;

public class GatewayProvider implements GatewayProviderInterface {
  public GatewayProvider(Engine engine) {
    throw new RuntimeException("GatewayProvider was not overridden!");
  }
  
  @Override public AccountGatewayInterface accountGateway() {
    return null;
  }
}