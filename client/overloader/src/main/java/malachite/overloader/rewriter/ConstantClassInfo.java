package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantClassInfo extends ConstantPool {
  public final short index;
  
  public ConstantClassInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    index = r.readU2();
    
    //System.out.println("Class name index: " + index);
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(index);
  }
}