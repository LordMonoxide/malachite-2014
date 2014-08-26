package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantInvokeDynamicInfo extends ConstantPool {
  public final short methodIndex;
  public final short nameTypeIndex;
  
  public ConstantInvokeDynamicInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    methodIndex   = r.readU2();
    nameTypeIndex = r.readU2();
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(methodIndex);
    out.putShort(nameTypeIndex);
  }
}