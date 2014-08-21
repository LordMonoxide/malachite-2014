package malachite.engine;

import malachite.engine.providers.GatewayProvider;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.providers.SecurityProvider;
import malachite.engine.providers.SecurityProviderInterface;

public final class Engine {
  private final Engine _this = this;;
  
  public final Config    config    = new Config();
  public final Providers providers = new Providers();
  
  public class Providers {
    public final GatewayProviderInterface  gateways = new GatewayProvider(_this);
    public final SecurityProviderInterface security = new SecurityProvider(_this);
    
    private Providers() { }
  }
}