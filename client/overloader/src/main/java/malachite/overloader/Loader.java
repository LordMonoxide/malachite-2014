package malachite.overloader;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import malachite.overloader.rewriter.ClassRewriter;

public class Loader extends ClassLoader {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  
  private static final String CLASS = ".class"; //$NON-NLS-1$
  private static final String JAVA  = ".java";  //$NON-NLS-1$
  
  private Map<String, String> _override = new HashMap<>();
  
  public Loader(ClassLoader parent) {
    super(parent);
  }
  
  @SuppressWarnings("unchecked")
  public <T> T create(String className) throws InstantiationException, IllegalAccessException, IOException {
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
        logger.debug("Overriding class '{}'", name); //$NON-NLS-1$
        
        try {
          c = getClass(name, override);
        } catch(IOException e) {
          e.printStackTrace();
        }
      } else {
        logger.trace("Using cached overridden class '{}'", name); //$NON-NLS-1$
      }
      
      return c;
    }
    
    logger.trace("Loading class '{}'", name); //$NON-NLS-1$
    
    //TODO
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
  
  private Class<?> getClass(String name, String override) throws IOException {
    String file = (override != null ? override : name).replace('.', '/') + CLASS;
    byte[] b = null;
    
    if(override != null) {
      ClassRewriter r = new ClassRewriter(getClass().getClassLoader().getResourceAsStream(file));
      r.parse();
      r.rewrite(override.replace('.', '/'), name.replace('.', '/'));
      r.rewrite(override.substring(override.lastIndexOf('.') + 1) + JAVA, name.substring(name.lastIndexOf('.') + 1) + JAVA);
      b = r.commit();
    } else {
      b = loadClassData(file);
    }
    
    if(b != null) {
      Class<?> c = defineClass(name, b, 0, b.length);
      resolveClass(c);
      return c;
    } else {
      return null;
    }
  }
  
  private byte[] loadClassData(String name) throws IOException {
    try(InputStream stream = getClass().getClassLoader().getResourceAsStream(name)) {
      if(stream != null) {
        int size = stream.available();
        byte buff[] = new byte[size];
        
        try(DataInputStream in = new DataInputStream(stream)) {
          in.readFully(buff);
          return buff;
        }
      }
    }
    
    return null;
  }
}