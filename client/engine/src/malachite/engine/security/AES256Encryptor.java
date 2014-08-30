package malachite.engine.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES256Encryptor implements EncryptorInterface {
  private static final byte[] PHRASE = new byte[] {0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79};
  
  private final Cipher _encrypt, _decrypt;
  private final SecretKeySpec _key;
  
  public AES256Encryptor() throws Exception {
    _encrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
    _decrypt = Cipher.getInstance("AES/ECB/PKCS5Padding");
    _key    = new SecretKeySpec(PHRASE, "AES");
    _encrypt.init(Cipher.ENCRYPT_MODE, _key);
    _decrypt.init(Cipher.DECRYPT_MODE, _key);
  }
  
  @Override public Encryption encrypt(String data) throws Exception {
    return encrypt(data.getBytes());
  }
  
  @Override public Encryption encrypt(byte[] data) throws Exception {
    return new Encryption(this, _encrypt.doFinal(data));
  }
  
  @Override public byte[] decrypt(byte[] data) throws Exception {
    return _decrypt.doFinal(data);
  }
}