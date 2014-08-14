package malachite.engine.providers;

import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.gateways.HTTPAccountGateway;

public class HTTPGatewayProvider implements GatewayProviderInterface {
  private AccountGatewayInterface _account;
  
  @Override public AccountGatewayInterface accountGateway() {
    if(_account == null) { _account = new HTTPAccountGateway(); }
    return _account;
  }
}