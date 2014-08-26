package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Constant4ByteInfo extends ConstantPool {
  public final int value;
  
  public Constant4ByteInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    value = r.readU4();
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putInt(value);
  }
}