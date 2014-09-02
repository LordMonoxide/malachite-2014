package malachite.gfx;

import java.util.ArrayList;
import java.util.List;

import malachite.engine.Engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Manager {
  private static final Logger logger = LoggerFactory.getLogger(Manager.class);
  
  private static final List<Class<? extends Context>> _contexts = new ArrayList<>();
  
  private static Context _context;
  
  public static Context getContext() {
    return _context;
  }
  
  public static void registerContext(Class<? extends Context> context) {
    _contexts.add(context);
  }
  
  public static Context create(Engine engine, ContextInitializer initializer) {
    for(Class<? extends Context> c : _contexts) {
      try {
        _context = c.newInstance();
        
        if(initializer != null) {
          initializer.initialize(_context);
        }
        
        if(!_context.create(engine)) {
          continue;
        }
        
        return _context;
      } catch(InstantiationException | IllegalAccessException e) {
        logger.error("Error creating context", e); //$NON-NLS-1$
        e.printStackTrace();
      }
    }
    
    return null;
  }
  
  private Manager() { }
  
  public interface ContextInitializer {
    void initialize(Context ctx);
  }
}