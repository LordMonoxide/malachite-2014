package malachite.overloader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import malachite.overloader.rewriter.ClassRewriter;

public class Loader extends ClassLoader {
  private Map<String, String> _override = new HashMap<>();
  
  public Loader(ClassLoader parent) {
    super(parent);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T create(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
    return (T)getClass(className, null).newInstance();
  }
  
  public void bind(String request, String override) {
    _override.put(request, override);
  }
  
  @Override public Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    String override = _override.get(name);
    if(override != null) {
      Class<?> c = findLoadedClass(override);
      if(c == null) {
        System.out.println("Overriding class '" + name + '\'');
        
        try {
          c = getClass(name, override);
        } catch(IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Using cached overridden class '" + name + '\'');
      }
      
      return c;
    }
    
    System.out.println("Loading class '" + name + '\'');
    
    //if(name.startsWith("java") || name.startsWith("sun.")) {
    if(!(name.startsWith("malachite.") || name.startsWith("game."))) {
      return super.loadClass(name, false);
    }
    
    try {
      return getClass(name, null);
    } catch(IOException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  private Class<?> getClass(String name, String override) throws ClassNotFoundException, IOException {
    String file = (override != null ? override : name).replace('.', '/') + ".class";
    byte[] b = null;
    
    if(override != null) {
      ClassRewriter r = new ClassRewriter(getClass().getClassLoader().getResourceAsStream(file));
      r.parse();
      r.rewrite(override.replace('.', '/'), name.replace('.', '/'));
      r.rewrite(override.substring(override.lastIndexOf('.') + 1) + ".java", name.substring(name.lastIndexOf('.') + 1) + ".java");
      b = r.commit();
    } else {
      b = loadClassData(file);
    }
    
    Class<?> c = defineClass(name, b, 0, b.length);
    resolveClass(c);
    return c;
  }
  
  private byte[] loadClassData(String name) throws IOException {
    InputStream stream = getClass().getClassLoader().getResourceAsStream(name);
    int size = stream.available();
    byte buff[] = new byte[size];
    DataInputStream in = new DataInputStream(stream);
    in.readFully(buff);
    in.close();
    return buff;
  }
}