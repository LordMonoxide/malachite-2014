package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantNameAndTypeInfo extends ConstantPool {
  public short nameIndex;
  public short descriptorIndex;
  
  public ConstantNameAndTypeInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    nameIndex       = r.readU2();
    descriptorIndex = r.readU2();
    
    /*System.out.println("Name index: " + nameIndex);
    System.out.println("Descriptor index: " + descriptorIndex);*/
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(nameIndex);
    out.putShort(descriptorIndex);
  }
}
