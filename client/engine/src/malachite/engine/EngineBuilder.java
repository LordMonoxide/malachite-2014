package malachite.engine;

import malachite.engine.providers.HTTPGatewayProvider;
import malachite.engine.providers.GatewayProviderInterface;

public final class EngineBuilder {
  @Override public String toString() {
    return "-- Malachite engine --\n" +
           "Gateway Prodiver: " + _gatewayProvider;
  }
  
  private GatewayProviderInterface _gatewayProvider;
  
  public EngineBuilder() {
    _gatewayProvider = new HTTPGatewayProvider();
  }
  
  public EngineBuilder withGatewayProvider(GatewayProviderInterface gatewayProvider) {
    _gatewayProvider = gatewayProvider;
    return this;
  }
  
  public Engine build() {
    return new Engine(_gatewayProvider);
  }
}