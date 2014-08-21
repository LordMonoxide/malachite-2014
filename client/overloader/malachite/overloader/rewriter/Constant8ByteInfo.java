package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Constant8ByteInfo extends ConstantPool {
  public final int valueHi;
  public final int valueLo;
  
  public Constant8ByteInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    valueHi = r.readU4();
    valueLo = r.readU4();
  }

  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putInt(valueHi);
    out.putInt(valueLo);
  }
}