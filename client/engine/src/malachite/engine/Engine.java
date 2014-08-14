package malachite.engine;

import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.providers.GatewayProviderInterface;

public final class Engine {
  public final AccountGatewayInterface accountGateway;
  
  Engine(GatewayProviderInterface gatewayProvider) {
    accountGateway = gatewayProvider.accountGateway();
  }
}