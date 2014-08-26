package malachite.gfx;

import malachite.gfx.util.Time;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Logic {
  private static final Logger logger = LoggerFactory.getLogger(Logic.class);
  
  private Context _context;

  private Thread _thread;

  private boolean _running;
  private boolean _finished = true;

  private double _fps;

  private boolean[] _keyDown = new boolean[256];
  
  public boolean isRunning () { return _running; }
  public boolean isFinished() { return _finished; }
  public double  fps       () { return _fps; }

  Logic(Context context) {
    _context = context;
  }

  public void start() {
    if(_thread != null) { return; }
    
    _thread = new Thread(() -> {
      logger.info("Logic thread started."); //$NON-NLS-1$
      
      _running = true;
      _fps = 120;
      
      double logicTimeout = Time.HzToTicks(_fps);
      double logicTimer   = Time.get();

      double inputTimeout = Time.HzToTicks(60);
      double inputTimer   = Time.get();

      double fpsTimer = Time.get();

      while(_running) {
        if(inputTimer <= Time.get()) {
          inputTimer += inputTimeout;
          keyboard();
        }

        if(logicTimer <= Time.get()) {
          logicTimer += logicTimeout;
          _context._gui.logic();
          
          _fps = 1000 / (Time.get() - fpsTimer);
          fpsTimer = Time.get();
        }

        try {
          Thread.sleep(1);
        } catch(InterruptedException e) { }
      }

      _finished = true;

      logger.info("Logic thread finished."); //$NON-NLS-1$
    });
    
    _thread.start();
  }

  public void stop() {
    _running = false;
  }

  protected void keyboard() {
    if(Keyboard.next()) {
      if(Keyboard.getEventKeyState()) {
        _context._gui.keyDown(Keyboard.getEventKey(), _keyDown[Keyboard.getEventKey()]);
        _keyDown[Keyboard.getEventKey()] = true;

        if(Keyboard.getEventCharacter() != 0) {
          switch(Keyboard.getEventCharacter()) {
            case  8: case  9:
            case 13: case 27:
              break;

            default:
              _context._gui.charDown(Keyboard.getEventCharacter());
          }
        }
      } else {
        _keyDown[Keyboard.getEventKey()] = false;
        _context._gui.keyUp(Keyboard.getEventKey());
      }
    }
  }
}