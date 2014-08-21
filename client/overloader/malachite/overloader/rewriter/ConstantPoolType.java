package malachite.overloader.rewriter;

import java.lang.reflect.InvocationTargetException;

enum ConstantPoolType {
  UTF8      ((byte)0x01, "UTF-8",          ConstantUTF8Info.class),
  INTEGER   ((byte)0x03, "Integer",        Constant4ByteInfo.class),
  FLOAT     ((byte)0x04, "Float",          Constant4ByteInfo.class),
  LONG      ((byte)0x05, "Long",           Constant8ByteInfo.class),
  DOUBLE    ((byte)0x06, "Double",         Constant8ByteInfo.class),
  CLASS     ((byte)0x07, "Class",          ConstantClassInfo.class),
  RSTRING   ((byte)0x08, "String Ref",     ConstantStringInfo.class),
  RFIELD    ((byte)0x09, "Field Ref",      ConstantReferenceInfo.class),
  RMETHOD   ((byte)0x0A, "Method Ref",     ConstantReferenceInfo.class),
  RINTERFACE((byte)0x0B, "Interface Ref",  ConstantReferenceInfo.class),
  NAMETYPE  ((byte)0x0C, "Name/Type",      ConstantNameAndTypeInfo.class),
  HMETHOD   ((byte)0x0F, "Method Handle",  ConstantMethodHandle.class),
  TMETHOD   ((byte)0x10, "Method Type",    ConstantMethodType.class),
  INVOKE    ((byte)0x12, "Invoke Dynamic", ConstantInvokeDynamicInfo.class);
  
  private byte   _val;
  private String _name;
  private Class<? extends ConstantPool> _class;
  
  ConstantPoolType(byte val, String name, Class<? extends ConstantPool> c) {
    _val   = val;
    _name  = name;
    _class = c;
  }
  
  @Override public String toString() {
    return _name;
  }
  
  public byte index() {
    return _val;
  }
  
  public ConstantPool instanciate(ClassRewriter r) {
    try {
      return _class.getConstructor(ClassRewriter.class, ConstantPoolType.class).newInstance(r, this);
    } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  public static ConstantPoolType of(int type) {
    for(ConstantPoolType t : ConstantPoolType.values()) {
      if(t._val == type) {
        return t;
      }
    }
    
    return null;
  }
}