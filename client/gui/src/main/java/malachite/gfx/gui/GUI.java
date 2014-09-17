package malachite.gfx.gui;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.Context;
import malachite.gfx.ContextEvents.DrawEventData;
import malachite.gfx.ContextEvents.MouseButtonEventData;
import malachite.gfx.ContextEvents.MouseMoveEventData;
import malachite.gfx.ContextEvents.MouseWheelEventData;
import malachite.gfx.Matrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GUI {
  private static final Logger logger = LoggerFactory.getLogger(GUI.class);
  
  public final Events events = new Events();
  
  protected final Context _ctx;
  protected final Matrix  _matrix;
  protected final GUIManager _guis;
  
  protected final Control<? extends ControlEvents> _control;
  
  private Control<? extends ControlEvents> _focus;
  private Control<? extends ControlEvents> _keyDownControl;
  private Control<? extends ControlEvents> _selectControl;
  private Control<? extends ControlEvents> _selectControlMove;
  
  private int _selectButton = -1;
  private int _mouseX, _mouseY;
  
  private boolean _loaded;
  
  protected GUI(Context ctx, GUIManager guis) {
    _ctx    = ctx;
    _matrix = ctx.matrix;
    _guis   = guis;
    
    GUI _this = this;
    _control = new Control<ControlEvents>(ctx) {
      { _gui = _this; }
      @Override protected void resize() { }
    };
  }
  
  public boolean isLoaded() { return _loaded; }
  
  protected void ready() {
    _ctx.threads.gfx(() -> {
      load();
      _loaded = true;
      events.raiseLoad();
    });
  }
  
  public ControlList controls() {
    return _control._controlList;
  }
  
  void setFocus(Control<? extends ControlEvents> control) {
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
  
  void setWH(int w, int h) {
    _control.bounds.wh.set(w, h);
  }
  
  protected abstract void load();
  protected abstract void destroy();
  protected abstract void resize();
  protected abstract void draw();
  protected abstract boolean logic();
  
  final boolean logicGUI() {
    boolean b = logic();
    _control.logicControl();
    return b;
  }
  
  final void drawGUI(DrawEventData ev) {
    draw();
    drawControls();
  }
  
  final void drawSelect() {
    _ctx.clearContext();
    _control.drawSelect();
  }
  
  final void drawControls() {
    _matrix.push(() -> {
      _matrix.reset();
      _control.draw();
    });
  }
  
  public void push() {
    _guis.push(this);
  }
  
  public void pop() {
    _guis.pop(this);
  }
  
  private Control<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_control != null) {
      return _control.getSelectControl(colour);
    }
    
    return null;
  }
  
  final boolean mouseMove(MouseMoveEventData ev) {
    boolean handled = false;
    
    _mouseX = ev.x;
    _mouseY = ev.y;
    
    if(_selectControl != null) {
      _selectControl.handleMouseMove(ev.x - (int)_selectControl.calculateTotalX(), ev.y - (int)_selectControl.calculateTotalY(), _selectButton);
      
      handled = true;
    } else {
      drawSelect();
      
      int[] pixel = _ctx.getPixel(ev.x, ev.y);
      
      if(pixel[0] != 0 || pixel[1] != 0 || pixel[2] != 0) {
        _selectControl = getSelectControl(pixel);
        
        if(_selectControl != _selectControlMove) {
          if(_selectControlMove != null) { _selectControlMove.handleMouseLeave(); }
          if(_selectControl     != null) { _selectControl.handleMouseEnter(); }
          _selectControlMove = _selectControl;
        }
        
        if(_selectControl != null) {
          _selectControl.handleMouseMove(ev.x - (int)_selectControl.calculateTotalX(), ev.y - (int)_selectControl.calculateTotalY(), _selectButton);
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
    
    return handleMouseMove(ev.x, ev.y, _selectButton) || handled;
  }
  
  final boolean mouseDown(MouseButtonEventData ev) {
    boolean handled = false;
    
    _selectButton = ev.button;
    
    drawSelect();
    
    int[] pixel = _ctx.getPixel(ev.x, ev.y);
    
    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);
      
      if(_selectControl != null) {
        if(_selectControl.acceptsFocus()) {
          _selectControl.setFocus(true);
        }
        
        //TODO: Should all these int casts really be here?
        _selectControl.handleMouseDown(ev.x - (int)_selectControl.calculateTotalX(), ev.y - (int)_selectControl.calculateTotalY(), ev.button);
        handled = true;
      } else {
        logger.error("Found no controls of colour ({}, {}, {})", Integer.valueOf(pixel[0]), Integer.valueOf(pixel[1]), Integer.valueOf(pixel[2])); //$NON-NLS-1$
      }
    }
    
    return handleMouseDown(ev.x, ev.y, ev.button) || handled;
  }
  
  final boolean mouseUp(MouseButtonEventData ev) {
    boolean handled = false;
    
    _selectButton = -1;
    
    if(_selectControl != null) {
      _selectControl.handleMouseUp(ev.x - (int)_selectControl.calculateTotalX(), ev.y - (int)_selectControl.calculateTotalY(), ev.button);
      _selectControl = null;
      handled = true;
    }
    
    return handleMouseUp(ev.x, ev.y, ev.button) || handled;
  }
  
  final boolean mouseWheel(MouseWheelEventData ev) {        
    boolean handled = false;
    
    drawSelect();
    
    int[] pixel = _ctx.getPixel(_mouseX, _mouseY);
    
    if(pixel[0] != 0 || pixel[1] != 0  || pixel[2] != 0) {
      _selectControl = getSelectControl(pixel);
      
      if(_selectControl != null) {
        _selectControl.handleMouseWheel(ev.delta);
        _selectControl = null;
        
        handled = true;
      }
    }
    
    return handleMouseWheel(ev.delta) || handled;
  }
  
  final boolean keyDown(int key, boolean repeat) {
    if(_focus != null) {
      _keyDownControl = _focus;
      _focus.handleKeyDown(key, repeat);
    }
    
    return handleKeyDown(key, repeat);
  }
  
  final boolean keyUp(int key) {
    if(_keyDownControl != null) {
      _keyDownControl.handleKeyUp(key);
    }
    
    return handleKeyUp(key);
  }
  
  final boolean charDown(char key) {
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
    private Deque<Runnable> _load = new ConcurrentLinkedDeque<>();
    
    private Events() { }
    
    public void addLoadHandler(Runnable e) {
      _load.add(e);
      
      if(_loaded) {
        raiseLoad();
      }
    }
    
    public void raiseLoad() {
      Runnable e = null;
      while((e = _load.poll()) != null) {
        e.run();
      }
    }
  }
}