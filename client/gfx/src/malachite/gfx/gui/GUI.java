package malachite.gfx.gui;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.Context;
import malachite.gfx.Loader;
import malachite.gfx.Matrix;
import malachite.gfx.textures.TextureBuilder;

import org.lwjgl.input.Keyboard;

public abstract class GUI {
  private static final float[] _clearColour = {0.0f, 0.0f, 0.0f, 1.1f};

  protected Matrix _matrix = Context.getMatrix();
  protected TextureBuilder _textures = TextureBuilder.getInstance();

  protected Events  _events;
  protected boolean _loaded;

  private boolean _visible = true;

  protected Context _context;
  private Control<? extends ControlEvents> _control;
  private Control<? extends ControlEvents> _focus;

  private Control<? extends ControlEvents> _keyDownControl;
  private Control<? extends ControlEvents> _selectControl;
  private Control<? extends ControlEvents> _selectControlMove;
  private int _selectButton = -1;
  private int _mouseX, _mouseY;

  private boolean _forceSelect;

  protected GUI() {
    _context = Context.getContext();
    _control = new Control<ControlEvents>() {
      @Override protected void resize() { }
    };
    _control._gui = this;
    
    _events = new Events();
  }
  
  protected void ready() {
    _context.addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
      load();
      _loaded = true;
      _events.raiseLoad();
    });
  }
  
  public Events events() {
    return _events;
  }

  public boolean getVisible() {
    return _visible;
  }

  public void setVisible(boolean visible) {
    _visible = visible;
  }

  protected Control<? extends ControlEvents> getFocus() {
    return _focus;
  }

  public ControlList controls() {
    return _control._controlList;
  }

  protected void setFocus(Control<? extends ControlEvents> control) {
    if(_selectControl == _focus) {
      _selectControl = null;
    }

    if(_focus != null) {
      Control<? extends ControlEvents> focus = _focus;
      _focus = null;
      focus.setFocus(false);
    }

    _focus = control;
  }

  protected void setWH(int w, int h) {
    _control.size.set(w, h);
  }

  protected abstract void load();
  public    abstract void destroy();
  protected abstract void resize();
  protected abstract void draw();
  protected abstract boolean logic();

  protected final boolean logicGUI() {
    boolean b = logic();
    _control.logicControl();
    return b;
  }

  protected final void drawGUI() {
    draw();
    drawControls();
  }

  protected final void drawSelect() {
    _context.clear(_clearColour);
    _control.drawSelect();
  }

  protected final void drawControls() {
    if(_visible) {
      _matrix.push();
      _matrix.reset();

      if(!_forceSelect) {
        _control.draw();
      } else {
        drawSelect();
      }

      _matrix.pop();
    }
  }

  public void push() {
    _context.GUIs().push(this);
  }

  public void pop() {
    _context.GUIs().pop(this);
  }

  private Control<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_control != null) {
      return _control.getSelectControl(colour);
    }
    
    return null;
  }

  protected final boolean mouseDown(int x, int y, int button) {
    boolean handled = false;
    
    _selectButton = button;

    drawSelect();

    int[] pixel = _context.getPixel(x, y);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        if(_selectControl.acceptsFocus()) {
          _selectControl.setFocus(true);
        }

        //TODO: Should all these int casts really be here?
        _selectControl.handleMouseDown(x - (int)_selectControl.calculateTotalX(), y - (int)_selectControl.calculateTotalY(), button);
        handled = true;
      } else {
        System.err.println("Found no controls of this colour"); //$NON-NLS-1$
      }
    }

    return handleMouseDown(x, y, button) || handled;
  }

  protected final boolean mouseUp(int x, int y, int button) {
    boolean handled = false;
    
    _selectButton = -1;

    if(_selectControl != null) {
      _selectControl.handleMouseUp(x - (int)_selectControl.calculateTotalX(), y - (int)_selectControl.calculateTotalY(), button);
      _selectControl = null;
      handled = true;
    }

    return handleMouseUp(x, y, button) || handled;
  }

  protected final boolean mouseMove(int x, int y) {
    boolean handled = false;
    
    _mouseX = x;
    _mouseY = y;

    if(_selectControl != null) {
      _selectControl.handleMouseMove(x - (int)_selectControl.calculateTotalX(), y - (int)_selectControl.calculateTotalY(), _selectButton);
      
      handled = true;
    } else {
      drawSelect();

      int[] pixel = _context.getPixel(x, y);

      if(pixel[0] != 0 || pixel[1] != 0 || pixel[2] != 0) {
        _selectControl = getSelectControl(pixel);

        if(_selectControl != _selectControlMove) {
          if(_selectControlMove != null) { _selectControlMove.handleMouseLeave(); }
          if(_selectControl     != null) { _selectControl.handleMouseEnter(); }
          _selectControlMove = _selectControl;
        }

        if(_selectControl != null) {
          _selectControl.handleMouseMove(x - (int)_selectControl.calculateTotalX(), y - (int)_selectControl.calculateTotalY(), _selectButton);
          _selectControl = null;
          
          handled = true;
        }
      } else {
        if(_selectControlMove != null) {
          _selectControlMove.handleMouseLeave();
          _selectControlMove = null;
        }
      }
    }

    return handleMouseMove(x, y, _selectButton) || handled;
  }

  protected final boolean mouseWheel(int delta) {        
    boolean handled = false;

    drawSelect();

    int[] pixel = _context.getPixel(_mouseX, _mouseY);

    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);

      if(_selectControl != null) {
        _selectControl.handleMouseWheel(delta);
        _selectControl = null;
        
        handled = true;
      }
    }

    return handleMouseWheel(delta) || handled;
  }

  protected final boolean keyDown(int key, boolean repeat) {
    if(key == Keyboard.KEY_F12) {
      _forceSelect = !_forceSelect;

      if(_forceSelect) {
        System.out.println("Switching GUI render mode to select"); //$NON-NLS-1$
      } else {
        System.out.println("Switching GUI render mode to normal"); //$NON-NLS-1$
      }
    }

    if(_focus != null) {
      _keyDownControl = _focus;
      _focus.handleKeyDown(key, repeat);
    }

    return handleKeyDown(key, repeat);
  }

  protected final boolean keyUp(int key) {
    if(_keyDownControl != null) {
      _keyDownControl.handleKeyUp(key);
    }

    return handleKeyUp(key);
  }

  protected final boolean charDown(char key) {
    if(_focus != null) {
      _focus.handleCharDown(key);
    }

    return handleCharDown(key);
  }

  protected boolean handleMouseDown (int x, int y, int button) { return false; }
  protected boolean handleMouseUp   (int x, int y, int button) { return false; }
  protected boolean handleMouseMove (int x, int y, int button) { return false; }
  protected boolean handleMouseWheel(int delta)                { return false; }
  protected boolean handleKeyDown   (int key, boolean repeat)  { return false; }
  protected boolean handleKeyUp     (int key)  { return false; }
  protected boolean handleCharDown  (char key) { return false; }

  public class Events {
    private Deque<Event> _load = new ConcurrentLinkedDeque<>();

    private Events() {
    }

    public void addLoadHandler(Event e) {
      _load.add(e);

      if(_loaded) {
        raiseLoad();
      }
    }

    public void raiseLoad() {
      Event e = null;
      while((e = _load.poll()) != null) {
        e.run();
      }
    }
  }
  
  public interface Event {
    void run();
  }
}