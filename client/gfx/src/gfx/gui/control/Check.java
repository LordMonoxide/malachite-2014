package gfx.gui.control;

import gfx.Context;
import gfx.Drawable;
import gfx.fonts.Font;
import gfx.fonts.FontBuilder;
import gfx.fonts.TextStream;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.GUI;
import gfx.textures.Texture;
import gfx.textures.TextureBuilder;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.lwjgl.input.Keyboard;

public class Check extends Control<Check.Events> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _textStream = new TextStream();
  private TextStream.Text  _text = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  private float _textX, _textY;
  private int _textW, _textH;
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/check.png");
  private Texture _textureHover = _textures.getTexture("gui/check_hover.png");
  
  private Drawable _bg, _check;
  private boolean _checked;
  
  public Check() {
    super(
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER
    );
    
    _textStream.insert(_textColour);
    _textStream.insert(_text);
    
    _events = new Events(this);
    
    _events.onFocusGot(e -> {
      _border.setColour(_hoverBorder);
      _border.createBorder();
    }).onFocusLost(e -> {
      _border.setColour(_normalBorder);
      _border.createBorder();
    });
    
    _events.onHoverIn(e -> {
      _bg.setTexture(_textureHover);
    }).onHoverOut(e -> {
      _bg.setTexture(_textureNormal);
    });
    
    _events.onClick(e -> {
      toggle();
    });
    
    _events.onKeyDown(e -> {
      if(e.key == Keyboard.KEY_SPACE) {
        _events.onMouseDown(0, 0, -1);
      }
    }).onKeyUp(e -> {
      if(e.key == Keyboard.KEY_SPACE) {
        _events.onMouseUp(0, 0, -1);
        _events.onClick();
      }
    });
    
    _padW = 0;
    _padH = 0;
    
    _border.setColour(_normalBorder);
    
    _bg = Context.newDrawable();
    _bg.setTexture(_textureNormal);
    
    Texture check = _textures.getTexture("gui/check_check.png");
    _check = Context.newDrawable();
    _check.setTexture(check);
    _check.setVisible(false);
    
    _bg.setTWH(14, 14);
    _bg.setWH(14, 14);
    _bg.createQuad();
    _needsUpdate = true;
    
    _check.setTWH(14, 14);
    _check.setWH(14, 14);
    _check.createQuad();
    resize();
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }
  
  public void toggle() {
    setChecked(!_checked);
  }
  
  public void check() {
    setChecked(true);
  }
  
  public void uncheck() {
    setChecked(false);
  }
  
  public void setChecked(boolean checked) {
    if(checked == _checked) { return; }
    _checked = checked;
    _check.setVisible(_checked);
    
    events().onChange(_checked);
  }
  
  public boolean isChecked() {
    return _checked;
  }
  
  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text.setText(text);
      _textW = _font.regular().getW(text);
      _textH = _font.regular().getH();
    });
  }
  
  public String getText() {
    return _text.getText();
  }
  
  @Override protected void resize() {
    if(size.getX() < _bg.getW() + _textW + 2) {
      size.setX(_bg.getW() + _textW + 2);
    }
    
    if(size.getY() < _bg.getH()) {
      size.setY(_bg.getH());
    }
    
    switch(_hAlign) {
      case ALIGN_LEFT:   _bg.setX(_padW); break;
      case ALIGN_CENTER: _bg.setX(size.getX() - (_textW + _bg.getW()) / 2);    break;
      case ALIGN_RIGHT:  _bg.setX(size.getX() -  _textW - _bg.getW() - _padW); break;
    }
    
    switch(_vAlign) {
      case ALIGN_TOP:    _bg.setY(_padH); _textY = _padH; break;
      case ALIGN_MIDDLE: _bg.setY((size.getY() - _bg.getH()) / 2); _textY = (size.getY() - _textH) / 2;    break;
      case ALIGN_BOTTOM: _bg.setY( size.getY() - _bg.getH());      _textY =  size.getY() - _textH - _padH; break;
    }
    
    _check.setXY(_bg.getX(), _bg.getY());
    _border.setXYWH(_bg.getX() + 1, _bg.getY() + 1, 12, 12);
    _border.createBorder();
    
    _textX = (_bg.getX() + _bg.getW()) + 2;
  }
  
  @Override public void draw() {
    if(drawBegin()) {
      _bg.draw();
      _check.draw();
      _font.draw(_textX, _textY, _textStream);
    }
    
    drawEnd();
    drawNext();
  }
  
  public static class Events extends ControlEvents {
    private Deque<ChangeEvent> _change = new ConcurrentLinkedDeque<>();
    
    public Events onChange(ChangeEvent e) { _change.add(e); return this; }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void onChange(boolean checked) {
      for(ChangeEvent e : _change) {
        e.event(new ChangeEventData(_control, checked));
      }
    }
    
    public interface ChangeEvent extends Event<ChangeEventData> { }
    
    public class ChangeEventData extends EventData {
      public final boolean checked;
      
      private ChangeEventData(Control<? extends ControlEvents> control, boolean checked) {
        super(control);
        this.checked = checked;
      }
    }
  }
}