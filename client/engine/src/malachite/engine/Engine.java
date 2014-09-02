package malachite.engine;

import malachite.engine.providers.GatewayProvider;
import malachite.engine.providers.GatewayProviderInterface;
import malachite.engine.providers.LangProvider;
import malachite.engine.providers.LangProviderInterface;
import malachite.engine.providers.SecurityProvider;
import malachite.engine.providers.SecurityProviderInterface;

public final class Engine {
  @Override public String toString() {
    return "-- Malachite engine --\n" + //$NON-NLS-1$
           providers + '\n' +
           "---"; //$NON-NLS-1$
  }
  
  private final Engine _this = this;;
  
  public final Config    config    = new Config();
  public final Providers providers = new Providers();
  
  public class Providers {
    @Override public String toString() {
      return "- Providers -\n" + //$NON-NLS-1$
             "Gateway Provider:  " + gateways + //$NON-NLS-1$
           "\nSecurity Provider: " + security + //$NON-NLS-1$
           "\nLang Provider:     " + lang;      //$NON-NLS-1$
    }
    
    public final GatewayProviderInterface  gateways = new GatewayProvider(_this);
    public final SecurityProviderInterface security = new SecurityProvider(_this);
    public final LangProviderInterface     lang     = new LangProvider(_this);
    
    private Providers() { }
  }
}