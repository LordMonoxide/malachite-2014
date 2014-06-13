package malachite.engine.physics;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.engine.util.Time;

public class Sandbox {
  private Deque<Movable> _movable = new ConcurrentLinkedDeque<>();
  
  private Thread  _thread;
  private boolean _running;
  private double  _fps;
  
  public double fps() { return _fps; }
  
  public boolean add   (Movable m) { return _movable.add(m); }
  public boolean remove(Movable m) { return _movable.remove(m); }
  
  public void start() {
    if(_thread == null) {
      _thread = new Thread(() -> {
        _running = true;
        _fps = 120;
        
        double timeout = Time.HzToTicks(_fps);
        double timer   = Time.get();
        
        double fpsTimer = Time.get();
        
        while(_running) {
          if(timer <= Time.get()) {
            timer += timeout;
            
            for(Movable m : _movable) {
              if(m._velTarget != 0) {
                if(m._vel < m._velTerm) {
                  m.setVel(m._vel + m._acc);
                  if(m._vel > m._velTerm) m.setVel(m._velTerm);
                }
              } else {
                if(m._vel > 0) {
                  m.setVel(m._vel - m._dec);
                  if(m._vel < 0) m.setVel(0);
                }
              }
              
              if(m._vel != 0) {
                m.setX(m._x + (float)Math.cos(m._bear) * m._vel);
                m.setY(m._y + (float)Math.sin(m._bear) * m._vel);
              }
            }
          }
          
          _fps = 1 / (Time.get() - fpsTimer);
          fpsTimer = Time.get();
          
          try {
            Thread.sleep(1);
          } catch(InterruptedException e) { }
        }
      });
      
      _thread.start();
    }
  }
  
  public void stop() {
    _running = false;
  }
}