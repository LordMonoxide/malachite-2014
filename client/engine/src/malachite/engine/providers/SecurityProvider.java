package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.security.EncryptorInterface;
import malachite.engine.security.HasherInterface;

public class SecurityProvider implements SecurityProviderInterface {
  public SecurityProvider(Engine engine) {
    throw new RuntimeException("SecurityProvider was not overridden!");
  }
  
  @Override public HasherInterface hasher() {
    return null;
  }
  
  @Override public EncryptorInterface encryptor() {
    return null;
  }
}