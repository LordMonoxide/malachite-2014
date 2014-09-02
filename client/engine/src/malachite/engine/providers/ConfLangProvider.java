package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.lang.ConfLangGateway;
import malachite.engine.lang.LangGatewayInterface;

public class ConfLangProvider implements LangProviderInterface {
  @Override public String toString() {
    return "ConfLangProvider (" + super.toString() + ')'; //$NON-NLS-1$
  }
  
  private LangGatewayInterface _lang;
  
  public ConfLangProvider(Engine engine) {
    
  }
  
  @Override public LangGatewayInterface lang() {
    if(_lang == null) { _lang = new ConfLangGateway(); }
    return null;
  }
}