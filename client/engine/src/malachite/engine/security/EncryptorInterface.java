package malachite.engine.security;

public interface EncryptorInterface {
  public Encryption encrypt(byte[] data) throws Exception;
  public Encryption encrypt(String data) throws Exception;
  
  byte[] decrypt(byte[] data) throws Exception;
}