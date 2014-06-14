package malachite.gfx;

import java.util.ArrayList;
import java.util.List;

public final class Manager {
  private static final List<Class<? extends Context>> _contexts = new ArrayList<>();
  
  private static Context _context;
  
  public static Context getContext() {
    return _context;
  }
  
  public static void registerContext(Class<? extends Context> context) {
    _contexts.add(context);
  }
  
  public static Context create(ContextInitializer initializer) {
    for(Class<? extends Context> c : _contexts) {
      try {
        _context = c.newInstance();
        
        if(initializer != null) {
          initializer.initialize(_context);
        }
        
        _context.create();
        return _context;
      } catch(InstantiationException | IllegalAccessException e) {
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