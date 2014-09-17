package malachite.gfx.gui;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.Matrix;
import malachite.gfx.util.Bounds;
import malachite.gfx.util.HAlign;
import malachite.gfx.util.VAlign;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Control<T extends ControlEvents> {
  private static final Logger logger = LoggerFactory.getLogger(Control.class);
  
  protected Matrix _matrix = Context.getMatrix();

  protected Drawable _background;
  protected Drawable _border;
  
  public final Bounds bounds = new ControlBounds();
  
  protected HAlign _hAlign = HAlign.ALIGN_LEFT;
  protected VAlign _vAlign = VAlign.ALIGN_MIDDLE;

  protected int _padW = 2;
  protected int _padH = 2;

  protected boolean _needsUpdate;
  protected int _disabled;
  protected boolean _visible = true;
  protected boolean _focus;

  GUI _gui;

  ControlList _controlList = new ControlList(this);
  Control<? extends ControlEvents> _controlParent;
  Control<? extends ControlEvents> _controlNext;
  Control<? extends ControlEvents> _controlPrev;

  private boolean _acceptsFocus;

  protected Drawable _selBox;
  protected int[] _selColour;

  protected ControlEvents _events;

  protected Control(InitFlags... flags) {
    for(InitFlags flag : flags) {
      switch(flag) {
        case WITH_BACKGROUND:
          _background = Context.newDrawable();
          _background.setColour(new float[] {0.5f, 0.5f, 0.5f, 1});

        case WITH_BORDER:
          _border = Context.newDrawable();
          _border.setColour(new float[] {0, 0, 0, 1});
          _border.setXY(-1, -1);
          break;

        case WITH_DEFAULT_EVENTS:
          _events = new ControlEvents(this);
          break;

        case ACCEPTS_FOCUS:
          _acceptsFocus = true;
          break;

        case REGISTER:
          _selBox = Context.newDrawable();
          _selColour = Context.getContext().getNextSelectColour();

          float[] floatColour = new float[4];

          for(int i = 0; i < floatColour.length; i++) {
            floatColour[i] = _selColour[i] / 255f;
          }

          _selBox.setColour(floatColour);
          break;
      }
    }
  }

  protected void setGUI(GUI gui) {
    _gui = gui;
    
    Control<? extends ControlEvents> c = _controlList.first();
    while(c != null) {
      c.setGUI(gui);
      c = c._controlPrev;
    }
  }

  public ControlList controls() {
    return _controlList;
  }
  
  public Control<? extends ControlEvents> controlNext() {
    return _controlNext;
  }
  
  public Control<? extends ControlEvents> controlPrev() {
    return _controlPrev;
  }

  @SuppressWarnings("unchecked")
  public T events() {
    return (T)_events;
  }

  public final float calculateTotalX() {
    float x = bounds.getX();
    
    Control<? extends ControlEvents> c = _controlParent;
    while(c != null) {
      x += c.bounds.getX();
      c = c._controlParent;
    }
    
    return x;
  }

  public final float calculateTotalY() {
    float y = bounds.getY();
    
    Control<? extends ControlEvents> c = _controlParent;
    while(c != null) {
      y += c.bounds.getY();
      c = c._controlParent;
    }
    
    return y;
  }

  public void setBackground(Drawable d) {
    _background = d;
    _needsUpdate = true;
  }

  public Drawable getBackground() {
    return _background;
  }

  public void setBorderColour(float[] c) {
    _border.setColour(c);
    _needsUpdate = true;
  }

  public void setBorderColour(float r, float g, float b, float a) {
    _border.setColour(r, g, b, a);
    _needsUpdate = true;
  }

  public void setBackgroundColour(float[] c) {
    _background.setColour(c);
    _needsUpdate = true;
  }

  public void setBackgroundColour(float r, float g, float b, float a) {
    _background.setColour(r, g, b, a);
    _needsUpdate = true;
  }

  public final boolean acceptsFocus() {
    return _acceptsFocus;
  }

  public void setHAlign(HAlign align) {
    _hAlign = align;
  }

  public HAlign getHAlign() {
    return _hAlign;
  }

  public void setVAlign(VAlign align) {
    _vAlign = align;
  }

  public VAlign getVAlign() {
    return _vAlign;
  }

  public void enable() {
    _disabled--;
    _controlList.enable();
    
    if(_disabled < 0) {
      logger.error("You screwed up and enabled {} more times than it was disabled.", this); //$NON-NLS-1$
    }
  }

  public void disable() {
    _disabled++;
    _controlList.disable();
  }

  public boolean isEnabled() {
    return _disabled == 0;
  }

  public boolean isDisabled() {
    return _disabled != 0;
  }
  
  public int getDisabled() {
    return _disabled;
  }

  public void show() {
    _visible = true;
    
    if(_events != null) {
      _events.onShow();
    }
  }

  public void hide() {
    if(!_visible) { return; }
    _visible = false;
    
    if(_events != null) {
      _events.onHide();
    }
    
    setFocus(false);
    _controlList.killFocus();
  }

  public boolean isVisible() {
    return _visible;
  }

  public boolean isShown() {
    return _visible;
  }

  public boolean isHidden() {
    return !_visible;
  }
  
  public void setVisible(boolean visible) {
    if(visible) {
      show();
    } else {
      hide();
    }
  }

  public void setFocus(boolean focus) {
    if(_focus != focus) {
      if(focus) {
        _gui.setFocus(this);
        _focus = true;
        handleGotFocus();
      } else {
        _gui.setFocus(null);
        _focus = false;
        handleLostFocus();
      }
    }
  }

  public void handleKeyDown(int key, boolean repeat) {
    if(key == Keyboard.KEY_TAB) {
      Control<? extends ControlEvents> c = _controlNext;
      if(c == null) {
        if(_controlParent != null) {
          c = _controlParent._controlList.last();
        }
      }

      while(c != null) {
        if(c == this) { break; }

        if(c.acceptsFocus()) {
          c.setFocus(true);
          break;
        }
        
        c = c._controlNext;
        if(c == null) {
          if(_controlParent != null) {
            c = _controlParent._controlList.last();
          }
        }
      }
    }

    if(_events == null) { return; }
    
    if(!repeat) {
      _events.onKeyDown(key);
    } else {
      _events.onKeyRepeat(key);
    }
  }

  public void handleKeyUp(int key) {
    if(_events == null) { return; }
    _events.onKeyUp(key);
  }

  public void handleCharDown(char key) {
    if(_events == null) { return; }
    _events.onText(key);
  }

  public void handleMouseDown(int x, int y, int button) {
    if(_events == null) { return; }
    _events.onMouseDown(x, y, button);
  }

  public void handleMouseUp(int x, int y, int button) {
    if(_events == null) { return; }
    _events.onMouseUp(x, y, button);
    _events.onClick();
  }

  public void handleMouseMove(int x, int y, int button) {
    if(_events == null) { return; }
    _events.onMouseMove(x, y, button);
  }

  public void handleMouseWheel(int delta) {
    if(_events == null) { return; }
    _events.onScroll(delta);
  }

  public void handleMouseEnter() {
    if(_events == null) { return; }
    _events.onHoverIn();
  }

  public void handleMouseLeave() {
    if(_events == null) { return; }
    _events.onHoverOut();
  }

  public void handleGotFocus() {
    if(_events == null) { return; }
    _events.onFocusGot();
  }

  public void handleLostFocus() {
    if(_events == null) { return; }
    _events.onFocusLost();
  }

  private void updateSize() {
    if(_selBox != null) {
      _selBox.setWH(bounds.getW(), bounds.getH());
      _selBox.createQuad();
    }

    if(_background != null) {
      _background.setWH(bounds.getW() - _background.getX() * 2,
                        bounds.getH() - _background.getY() * 2);
      _background.createQuad();
    }

    if(_border != null) {
      _border.setWH(bounds.getW() - _border.getX() * 2,
                    bounds.getH() - _border.getY() * 2);
      _border.createBorder();
    }

    resize();

    if(_events != null) {
      _events.onResize();
    }

    _needsUpdate = false;
  }

  protected abstract void resize();

  protected final boolean drawBegin() {
    if(_visible) {
      if(_needsUpdate) {
        updateSize();
      }

      _matrix.push();
      _matrix.translate(bounds.xy);

      if(_background != null) {
        _background.draw();
      }

      return true;
    }

    return false;
  }

  protected final void drawEnd() {
    if(_visible) {
      if(_events != null) {
        _events.onDraw();
      }
      
      _controlList.draw();

      if(_border != null) {
        _border.draw();
      }

      _matrix.pop();
    }
  }
  
  protected final void drawNext() {
    if(_controlNext != null) {
      _controlNext.draw();
    }
  }

  public void draw() {
    drawBegin();
    drawEnd();
    drawNext();
  }

  public void logic() { }
  public void logicControl() {
    logic();
    _controlList.logic();

    if(_controlNext != null) {
      _controlNext.logicControl();
    }
  }

  public void drawSelect() {
    if(_visible && _disabled == 0) {
      if(_needsUpdate) {
        logger.trace("Updating {}", this); //$NON-NLS-1$
        updateSize();
      }

      _matrix.push();
      _matrix.translate(bounds.xy);

      if(_selBox != null) {
        _selBox.draw();
      }

      _controlList.drawSelect();

      _matrix.pop();
    }

    if(_controlNext != null) {
      _controlNext.drawSelect();
    }
  }

  public Control<? extends ControlEvents> getSelectControl(int[] colour) {
    if(_selBox != null && colour[0] == _selColour[0] && colour[1] == _selColour[1] && colour[2] == _selColour[2]) {
      return this;
    }
    
    Control<? extends ControlEvents> control = _controlList.getSelectControl(colour);
    if(control != null) {
      return control;
    }
    
    if(_controlNext != null) {
      return _controlNext.getSelectControl(colour);
    }

    return null;
  }

  public enum InitFlags {
    WITH_BACKGROUND,
    WITH_BORDER,
    WITH_DEFAULT_EVENTS,
    ACCEPTS_FOCUS,
    REGISTER
  }
  
  public enum PositionType {
    ABSOLUTE, FRACTIONAL
  }
  
  /*public class Position extends Point {
    private PositionType _typeX, _typeY;
    
    public Position(float x, float y) {
      _x = x;
      _y = y;
    }
    
    public PositionType getXType() { return _typeX; }
    public PositionType getYType() { return _typeY; }
    
    public void setXType(PositionType type) { _typeX = type; }
    public void setYType(PositionType type) { _typeY = type; }
  }
  
  public class Size extends Point {
    private PositionType _typeX, _typeY;
    
    public Size(float x, float y) {
      _x = x;
      _y = y;
    }
    
    @Override public void set(float x, float y) { super.set(x, y); _needsUpdate = true; }
    @Override public void setX(float x) { super.setX(x); _needsUpdate = true; }
    @Override public void setY(float y) { super.setY(y); _needsUpdate = true; }
    
    public PositionType getXType() { return _typeX; }
    public PositionType getYType() { return _typeY; }
    
    public void setXType(PositionType type) { _typeX = type; }
    public void setYType(PositionType type) { _typeY = type; }
  }*/
  
  public class ControlBounds extends Bounds {
    @Override protected void updateWH() {
      _needsUpdate = true;
    }
  }
}