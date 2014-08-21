package malachite.engine.providers;

import malachite.engine.security.HasherInterface;

public interface SecurityProviderInterface {
  public HasherInterface hasher();
}