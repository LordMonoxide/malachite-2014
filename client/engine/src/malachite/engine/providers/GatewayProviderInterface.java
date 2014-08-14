package malachite.engine.providers;

import malachite.engine.gateways.AccountGatewayInterface;

public interface GatewayProviderInterface {
  public AccountGatewayInterface accountGateway();
}