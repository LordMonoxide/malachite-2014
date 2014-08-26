package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantStringInfo extends ConstantPool {
  public final short stringIndex;
  
  public ConstantStringInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    stringIndex = r.readU2();
    
    //System.out.println("String index: " + stringIndex);
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(stringIndex);
  }
}
