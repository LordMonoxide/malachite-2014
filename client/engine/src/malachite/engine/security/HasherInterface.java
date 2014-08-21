package malachite.engine.security;

public interface HasherInterface {
  public String make(String password, String salt);
  public boolean check(String password, String hash);
}