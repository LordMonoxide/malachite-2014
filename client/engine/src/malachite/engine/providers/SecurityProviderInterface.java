package malachite.engine.providers;

import malachite.engine.security.EncryptorInterface;
import malachite.engine.security.HasherInterface;

public interface SecurityProviderInterface {
  public HasherInterface hasher();
  public EncryptorInterface encryptor();
}