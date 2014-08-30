package malachite.engine.security;

import java.util.Arrays;
import java.util.Base64;

public class Encryption {
  private final EncryptorInterface _encryptor;
  private final byte[] _data;
  
  Encryption(EncryptorInterface encryptor, byte[] data) {
    _encryptor = encryptor;
    _data = data;
  }
  
  public byte[] asBytes() {
    return Arrays.copyOf(_data, _data.length);
  }
  
  public String asString() {
    return Base64.getEncoder().encodeToString(_data);
  }
  
  public byte[] decrypt() throws Exception {
    return _encryptor.decrypt(_data);
  }
}