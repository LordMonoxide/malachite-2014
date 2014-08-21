package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.security.BCryptHasher;
import malachite.engine.security.HasherInterface;

public class DefaultSecurityProvider implements SecurityProviderInterface {
  private HasherInterface _hasher;
  
  public DefaultSecurityProvider(Engine engine) {
    
  }
  
  @Override public HasherInterface hasher() {
    if(_hasher == null) { _hasher = new BCryptHasher(); }
    return _hasher;
  }
}