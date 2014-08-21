package malachite.overloader.rewriter;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class ClassRewriter {
  private BufferedInputStream _in;
  
  private int          _magic;
  private short        _major;
  private short        _minor;
  private ConstantPool _cp[];
  private byte         _end[];
  
  public ClassRewriter(InputStream file) throws FileNotFoundException {
    _in = new BufferedInputStream(file);
  }
  
  public void parse() throws IOException {
    _magic = readU4();
    _minor = readU2();
    _major = readU2();
    
    int cpsize = readU2();
    
    /*System.out.println("Magic: "   + Integer.toHexString(_magic));
    System.out.println("Version: " + Integer.toHexString(_major) + '.' + Integer.toHexString(_minor));
    System.out.println("Constant pool size: " + Integer.toHexString(cpsize));*/
    
    _cp = new ConstantPool[cpsize - 1];
    
    for(int i = 0; i < _cp.length; i++) {
      int constant = readU1();
      //System.out.println((i + 1) + ": Constant " + constant);
      _cp[i] = ConstantPoolType.of(constant).instanciate(this);
    }
    
    _end = read();
  }
  
  public void rewrite(String from, String to) throws UnsupportedEncodingException {
    //System.out.println("Rewriting " + from + " to " + to);
    
    for(ConstantPool constant : _cp) {
      if(constant instanceof ConstantUTF8Info) {
        ConstantUTF8Info utf8 = (ConstantUTF8Info)constant;
        if(utf8.contains(from)) {
          utf8.replace(from, to);
        }
      }
    }
  }
  
  public byte[] commit() throws UnsupportedEncodingException {
    //System.out.println("Committing");
    
    ByteBuffer out = ByteBuffer.allocate(10000);
    out.putInt(_magic);
    out.putShort(_minor);
    out.putShort(_major);
    out.putShort((short)(_cp.length + 1));
    
    for(ConstantPool constant : _cp) {
      constant.put(out);
    }
    
    out.put(_end);
    
    out.flip();
    
    byte[] b = new byte[out.remaining()];
    out.get(b);
    
    /*for(int i = 0; i < b.length; i++) {
      System.out.print(Integer.toHexString(b[i] & 0x000000FF) + ' ');
    }*/
    
    return b;
  }
  
  byte[] read(int size) throws IOException {
    byte b[] = new byte[size];
    _in.read(b, 0, b.length);
    return b;
  }
  
  byte[] read() throws IOException {
    return read(_in.available());
  }
  
  private int readAsInt(int size) throws IOException {
    byte b[] = read(size);
    
    int n = 0;
    for(int i = 0; i < b.length; i++) {
      n += (b[i] & 0x000000FF) << ((b.length - i - 1) * 8);
    }
    
    return n;
  }
  
  byte readU1() throws IOException {
    return (byte)readAsInt(1);
  }
  
  short readU2() throws IOException {
    return (short)readAsInt(2);
  }
  
  int readU4() throws IOException {
    return readAsInt(4);
  }
}