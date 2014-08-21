package malachite.engine.security;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptHasher implements HasherInterface {
  @Override public String make(String password, String salt) {
    return BCrypt.hashpw(password, salt);
  }
  
  @Override public boolean check(String password, String hash) {
    return BCrypt.checkpw(password, hash);
  }
}