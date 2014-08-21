package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantMethodType extends ConstantPool {
  public final short index;
  
  public ConstantMethodType(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    index = r.readU2();
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(index);
  }
}