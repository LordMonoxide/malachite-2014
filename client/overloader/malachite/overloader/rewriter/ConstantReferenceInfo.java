package malachite.overloader.rewriter;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ConstantReferenceInfo extends ConstantPool {
  public final short classIndex;
  public final short nameAndTypeIndex;
  
  public ConstantReferenceInfo(ClassRewriter r, ConstantPoolType t) throws IOException {
    super(r, t);
    
    classIndex       = r.readU2();
    nameAndTypeIndex = r.readU2();
    
    /*System.out.println("Class index: " + classIndex);
    System.out.println("Name and type index: " + nameAndTypeIndex);*/
  }
  
  @Override void put(ByteBuffer out) {
    out.put(type.index());
    out.putShort(classIndex);
    out.putShort(nameAndTypeIndex);
  }
}