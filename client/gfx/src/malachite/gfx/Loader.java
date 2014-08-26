package malachite.gfx;

import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import malachite.gfx.util.Time;

public class Loader {
  private static final Logger logger = LoggerFactory.getLogger(Loader.class);
  
  Thread _thread;
  
  private boolean _running;
  private boolean _finished = true;
  
  private double _fps;
  
  private ConcurrentLinkedDeque<Callback> _cb = new ConcurrentLinkedDeque<>();
  
  public boolean isRunning () { return _running; }
  public boolean isFinished() { return _finished; }
  public double  fps       () { return _fps; }
  
  public void start() {
    if(_thread != null) { return; }
    
    _thread = new Thread(() -> {
      logger.info("Loader thread started."); //$NON-NLS-1$
      
      _running = true;
      _fps = 120;
      
      double fpsTimer = Time.get();
      
      while(_running) {
        Callback cb;
        
        if((cb = _cb.poll()) != null) {
          cb.load();
        }
        
        try {
          if(_cb.isEmpty()) {
            synchronized(_thread) {
              _thread.wait(1000);
            }
          } else {
            synchronized(_thread) {
              _thread.wait(10);
            }
          }
        } catch(InterruptedException e) { }
        
        _fps = 1000 / (Time.get() - fpsTimer);
        fpsTimer = Time.get();
      }
      
      _finished = true;
      synchronized(_thread) {
        _thread.notifyAll();
      }
      
      logger.info("Loader thread finished."); //$NON-NLS-1$
    });

    _thread.setPriority(Thread.MIN_PRIORITY);
    _thread.start();
  }
  
  public void stop() {
    _running = false;
    synchronized(_thread) {
      _thread.notifyAll();
    }
  }
  
  public void add(Callback cb) {
    _cb.add(cb);
    
    synchronized(_thread) {
      _thread.notifyAll();
    }
  }
  
  public interface Callback { void load(); }
  
  public enum LoaderThread {
    GRAPHICS,
    OFFLOAD
  }
}