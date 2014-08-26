package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class ConstantPool {
  public final ConstantPoolType type;
  
  public ConstantPool(ClassRewriter r, ConstantPoolType t) throws IOException {
    type = t;
    
    //System.out.println("Constant type: " + type);
  }
  
  abstract void put(ByteBuffer out);
}