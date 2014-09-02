package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.lang.LangGatewayInterface;

public class LangProvider implements LangProviderInterface {
  public LangProvider(Engine engine) {
    throw new RuntimeException("LangProvider was not overridden!");
  }
  
  @Override public LangGatewayInterface lang() {
    return null;
  }
}