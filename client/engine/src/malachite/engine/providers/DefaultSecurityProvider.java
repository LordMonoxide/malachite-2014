package malachite.engine.providers;

import malachite.engine.Engine;
import malachite.engine.security.AES256Encryptor;
import malachite.engine.security.BCryptHasher;
import malachite.engine.security.EncryptorInterface;
import malachite.engine.security.HasherInterface;

public class DefaultSecurityProvider implements SecurityProviderInterface {
  @Override public String toString() {
    return "DefaultSecurityProvider (" + super.toString() + ')'; //$NON-NLS-1$
  }
  
  private HasherInterface _hasher;
  private EncryptorInterface _encryptor;
  
  public DefaultSecurityProvider(Engine engine) {
    
  }
  
  @Override public HasherInterface hasher() {
    if(_hasher == null) { _hasher = new BCryptHasher(); }
    return _hasher;
  }
  
  @Override public EncryptorInterface encryptor() {
    if(_encryptor == null) {
      try {
        _encryptor = new AES256Encryptor();
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
    }
    
    return _encryptor;
  }
}