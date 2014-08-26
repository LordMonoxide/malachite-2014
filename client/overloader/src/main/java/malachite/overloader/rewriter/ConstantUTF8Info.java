package malachite.overloader.rewriter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ConstantUTF8Info extends ConstantPool {
  private short  length;
  private byte[] data;
  
  public ConstantUTF8Info(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    length = r.readU2();
    data   = r.read(length);
    
    //System.out.println("Read UTF-8 data: " + this);
  }
  
  public boolean contains(String data) {
    return toString().contains(data);
  }
  
  @Override public String toString() {
    try {
      return new String(data, "UTF-8");
    } catch(UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public void replace(String from, String to) throws UnsupportedEncodingException {
    data   = toString().replaceFirst(from + "\\b", to).getBytes("UTF-8");
    length = (short)data.length;
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(length);
    out.put(data);
  }
}