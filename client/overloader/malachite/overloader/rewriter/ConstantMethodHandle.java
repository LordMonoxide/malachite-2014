package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantMethodHandle extends ConstantPool {
  public final byte  kind;
  public final short index;
  
  public ConstantMethodHandle(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    kind  = r.readU1();
    index = r.readU2();
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.put(kind);
    out.putShort(index);
  }
}