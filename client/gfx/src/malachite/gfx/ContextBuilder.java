package malachite.gfx;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ContextBuilder {
  private static final Logger logger = LoggerFactory.getLogger(ContextBuilder.class);
  
  private final List<Class<? extends Context>> _contexts = new ArrayList<>();
  
  private String _title;
  private boolean _resizable = true;
  private int _w = 1280, _h = 720;
  private final float[] _clear = new float[] {0, 0, 0};
  private int _fps = 60;
  
  public ContextBuilder registerContext(Class<? extends Context> context) {
    _contexts.add(Objects.requireNonNull(context));
    return this;
  }
  
  public ContextBuilder setTitle(String title) {
    _title = title;
    return this;
  }
  
  public ContextBuilder setResizable(boolean resizable) {
    _resizable = resizable;
    return this;
  }
  
  public ContextBuilder setSize(int w, int h) {
    _w = w; _h = h;
    return this;
  }
  
  public ContextBuilder setClearColour(float r, float g, float b) {
    _clear[0] = r;
    _clear[1] = g;
    _clear[2] = b;
    return this;
  }
  
  public ContextBuilder setFPSLimit(int fps) {
    _fps = fps;
    return this;
  }
  
  public Context build() {
    if(_contexts.isEmpty()) {
      throw new NullPointerException("No context versions were registered before attempting to build a context"); //$NON-NLS-1$
    }
    
    for(Class<? extends Context> c : _contexts) {
      try {
        Context context = c.newInstance();
        
        if(!context.create(_title, _resizable, _clear, _w, _h, _fps)) {
          continue;
        }
        
        return context;
      } catch(InstantiationException | IllegalAccessException e) {
        logger.error("Error creating context", e); //$NON-NLS-1$
        e.printStackTrace();
      }
    }
    
    return null;
  }
}