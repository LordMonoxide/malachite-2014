package malachite.gfx;

import malachite.engine.util.Time;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Loader {
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
      System.out.println("Loader thread started."); //$NON-NLS-1$
      
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
      
      System.out.println("Loader thread finished."); //$NON-NLS-1$
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