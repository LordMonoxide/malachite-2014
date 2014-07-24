package gfx.gui.control;

import org.lwjgl.input.Keyboard;

import gfx.Context;
import gfx.Drawable;
import gfx.Scalable;
import gfx.fonts.Font;
import gfx.fonts.FontBuilder;
import gfx.fonts.TextStream;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.ControlEvents.FocusEventData;
import gfx.gui.ControlEvents.HoverEventData;
import gfx.gui.ControlEvents.KeyEventData;
import gfx.gui.ControlEvents.MouseEventData;
import gfx.gui.ControlEvents.TextEventData;
import gfx.gui.GUI;
import gfx.textures.Texture;
import gfx.textures.TextureBuilder;
import gfx.util.Time;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Textbox extends Control<Textbox.Events> {
  private static final String EMPTY = ""; //$NON-NLS-1$
  
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _placeholderStream = new TextStream();
  private TextStream.Text _placeholder = new TextStream.Text();
  private TextStream.Colour _placeholderColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 0.5f);
  private String[] _text = {EMPTY, EMPTY, EMPTY};
  private TextStream _textStream = new TextStream();
  private TextStream.Text _textFull = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  private int _mask;
  private float _textX, _textY;
  private int[] _textW = new int[3];
  private int _textH;
  
  private TextureBuilder _textures = TextureBuilder.getInstance();
  private Texture _textureNormal = _textures.getTexture("gui/textbox.png");
  private Texture _textureHover = _textures.getTexture("gui/textbox_hover.png");
  
  private float[] _normalBorder = {160f / 0xFF, 147f / 0xFF, 111f / 0xFF, 1};
  private float[] _hoverBorder  = { 11f / 0xFF, 126f / 0xFF,   0f / 0xFF, 1};
  
  private Drawable _caret;
  private double _caretPulse;
  
  private boolean _shift;
  private int _selectDirection;
  
  public Textbox() {
    super(
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER
    );
    
    _placeholderStream.insert(_placeholderColour);
    _placeholderStream.insert(_placeholder);
    _textStream.insert(_textColour);
    _textStream.insert(_textFull);
    
    _events = new Events(this);
    _events.onKeyDown  (new KeyDownHandler());
    _events.onKeyUp    (new KeyUpHandler());
    _events.onText     (new TextHandler());
    _events.onFocusGot (new FocusGotHandler());
    _events.onFocusLost(new FocusLostHandler());
    _events.onHoverIn  (new HoverInHandler());
    _events.onHoverOut (new HoverOutHandler());
    _events.onMouseMove(new MouseMoveHandler());
    _events.onMouseDown(new MouseDownHandler());
    _events.onMouseUp  (new MouseUpHandler());
    
    _caret = Context.newDrawable();
    _caret.setColour(new float[] {_textColour.getColour()[0], _textColour.getColour()[1], _textColour.getColour()[2], 1});
    
    _font.events().addLoadHandler(() -> {
      _caret.setWH(1, _font.regular().getH());
      _caret.createQuad();
      resize();
    });
    
    _border.setColour(_normalBorder);
    
    Scalable s = Context.newScalable();
    s.setTexture(_textureNormal);
    s.setXY(-5, -5);
    s.setSize(new float[] {12, 12, 12, 12},
      new float[] {12, 12, 12, 12},
      25, 25, 1
    );
    
    setBackground(s);
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }
  
  public void setText(String text) {
    if(_font.loaded()) {
      updateText(text == null ? EMPTY : text, EMPTY, EMPTY);
      _selectDirection = 0;
    } else {
      _font.events().addLoadHandler(() -> {
        updateText(text == null ? EMPTY : text, EMPTY, EMPTY);
        _selectDirection = 0;
      });
    }
  }
  
  private void updateText(String... text) {
    _text[0] = text[0];
    _text[1] = text[1];
    _text[2] = text[2];
    resize();
    resetCaretAlpha();
  }
  
  public String getText() {
    return _textFull.getText();
  }
  
  public void setPlaceholder(String text) {
    _placeholder.setText(text);
  }
  
  public String getPlaceholder() {
    return _placeholder.getText();
  }
  
  public void setMasked(boolean masked) {
    _mask = masked ? 0x2022 : 0;
  }
  
  public void mask  () { setMasked(true);  }
  public void unmask() { setMasked(false); }
  
  public boolean isMasked() {
    return _mask != 0;
  }
  
  public int getSelStart() {
    return _text[0].length();
  }
  
  public void setSelStart(int index) {
    setSelLength(0);
    updateText(_textFull.getText().substring(0, index), EMPTY, _textFull.getText().substring(index));
  }
  
  public int getSelLength() {
    return _text[1].length();
  }
  
  public void setSelLength(int length) {
    if(length == 0) {
      if(_selectDirection >= 0) {
        updateText(_text[0], EMPTY, _text[1] + _text[2]);
      } else {
        updateText(_text[0] + _text[1], EMPTY, _text[2]);
      }
    } else if(length > 0) {
      String s = _text[1] + _text[2];
      updateText(_text[0], s.substring(0, length), s.substring(length));
    } else {
      String s = _text[0] + _text[1];
      updateText(s.substring(0, s.length() + length), s.substring(s.length() + length, s.length()), _text[2]);
    }
    
    _selectDirection = (int)Math.signum(length);
  }
  
  @Override protected void resize() {
    String temp = EMPTY;
    for(int i = 0; i < _text.length; i++) {
      if(!_text[i].isEmpty()) {
        temp += _text[i];
      }
    }
    
    _textFull.setText(temp);
    
    _textW[0] = _font.regular().getW(_text[0], _mask);
    _textW[1] = _font.regular().getW(_text[1], _mask);
    _textW[2] = _font.regular().getW(_text[2], _mask);
    
    _textH = _font.regular().getH();
    _caret.setX(_textW[0]);
    
    if(_textW[1] == 0) {
      _caret.setW(1);
    } else {
      _caret.setW(_textW[1]);
    }
      
    _caret.createQuad();
    
    int totalW = _textW[0] + _textW[1] + _textW[2];
    
    switch(_hAlign) {
      case ALIGN_LEFT:   _textX = _padW; break;
      case ALIGN_CENTER: _textX = (_size.getX() - totalW) / 2; break;
      case ALIGN_RIGHT:  _textX =  _size.getX() - totalW - _padW; break;
    }
    
    switch(_vAlign) {
      case ALIGN_TOP:    _textY = _padH; break;
      case ALIGN_MIDDLE: _textY = (_size.getY() - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  _size.getY() - _textH - _padH; break;
    }
  }
  
  @Override public void draw() {
    if(drawBegin()) {
      _matrix.push();
      _matrix.translate(_textX, _textY);
      
      if(!_textFull.getText().isEmpty()) {
        _font.draw(0, 0, _textStream, _mask);
      } else {
        if(_placeholder.getText() != null && !_placeholder.getText().isEmpty()) {
          _font.draw(0, 0, _placeholderStream);
        }
      }
      
      if(_focus) {
        _caret.draw();
      }
      
      _matrix.pop();
    }
    
    drawEnd();
    drawNext();
  }
  
  @Override public void logic() {
    decrementCaretAlpha();
    
    if(_caretPulse <= Time.get()) {
      resetCaretAlpha();
    }
  }
  
  private void decrementCaretAlpha() {
    if(_caret.getColour()[3] > 0) {
      _caret.getColour()[3] -= 0.01;
    } else {
      _caret.getColour()[3] = 0;
    }
  }
  
  private void resetCaretAlpha() {
    _caret.getColour()[3] = 1;
    _caretPulse = Time.get() + Time.MSToTicks(1000);
  }
  
  private class KeyDownHandler implements ControlEvents.KeyEvent {
    @Override public void event(KeyEventData e) {
      switch(e.key) {
        case Keyboard.KEY_LSHIFT:
        case Keyboard.KEY_RSHIFT:
          _shift = true;
          break;
          
        case Keyboard.KEY_BACK:
          if(!_text[1].isEmpty()) {
            updateText(_text[0], EMPTY, _text[2]);
          } else {
            if(!_text[0].isEmpty()) {
              updateText(_text[0].substring(0, _text[0].length() - 1), EMPTY, _text[2]);
            }
          }

          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_DELETE:
          if(!_text[1].isEmpty()) {
            updateText(_text[0], EMPTY, _text[2]);
          } else {
            if(!_text[2].isEmpty()) {
              updateText(_text[0], EMPTY, _text[2].substring(1));
            }
          }
          
          _selectDirection = 0;
          break;
          
        case Keyboard.KEY_HOME:
          if(!_shift) {
            updateText(EMPTY, EMPTY, _text[0] + _text[1] + _text[2]);
            _selectDirection = 0;
          } else {
            if(_selectDirection == 1) {
              updateText(EMPTY, _text[0], _text[1] + _text[2]);
            } else {
              updateText(EMPTY, _text[0] + _text[1], _text[2]);
            }
            
            _selectDirection = -1;
          }
          
          break;
          
        case Keyboard.KEY_END:
          if(!_shift) {
            updateText(_text[0] + _text[1] + _text[2], EMPTY, EMPTY);
            _selectDirection = 0;
          } else {
            if(_selectDirection == -1) {
              updateText(_text[0] + _text[1], _text[2], EMPTY);
            } else {
              updateText(_text[0], _text[1] + _text[2], EMPTY);
            }
            
            _selectDirection = 1;
          }
          
          break;
          
        case Keyboard.KEY_LEFT:
          if(!_shift) {
            if(_selectDirection == 0) {
              String[] s = new String[] {_text[0] + _text[1], _text[2]};
              if(!s[0].isEmpty()) {
                s[1] = s[0].substring(s[0].length() - 1) + s[1];
                s[0] = s[0].substring(0, s[0].length() - 1);
                updateText(s[0], EMPTY, s[1]);
              }
            } else {
              updateText(_text[0], EMPTY, _text[1] + _text[2]);
              _selectDirection = 0;
            }
          } else {
            if(!_text[0].isEmpty()) {
              switch(_selectDirection) {
                case 0:
                  _selectDirection = -1;
                  // Yes, fall-through is intended
                  
                case -1:
                  updateText(_text[0].substring(0, _text[0].length() - 1), _text[0].substring(_text[0].length() - 1) + _text[1], _text[2]);
                  break;
                  
                case 1:
                  updateText(_text[0], _text[1].substring(0, _text[1].length() - 1), _text[1].substring(_text[1].length() - 1) + _text[2]);
                  
                  if(_text[1].isEmpty()) {
                    _selectDirection = 0;
                  }
                  
                  break;
              }
            }
          }
          
          break;
          
        case Keyboard.KEY_RIGHT:
          if(!_shift) {
            if(_selectDirection == 0) {
              String[] s = new String[] {_text[0] + _text[1], _text[2]};
              if(!s[1].isEmpty()) {
                s[0] += s[1].substring(0, 1);
                s[1] = s[1].substring(1);
                updateText(s[0], EMPTY, s[1]);
              }
            } else {
              updateText(_text[0] + _text[1], EMPTY, _text[2]);
              _selectDirection = 0;
            }
          } else {
            if(!_text[2].isEmpty()) {
              switch(_selectDirection) {
                case 0:
                  _selectDirection = 1;
                  // Yes, fall-through is intended
                  
                case 1:
                  updateText(_text[0], _text[1] + _text[2].substring(0, 1), _text[2].substring(1));
                  break;
                  
                case -1:
                  updateText(_text[0] + _text[1].substring(0, 1), _text[1].substring(1), _text[2]);
                  
                  if(_text[1].isEmpty()) {
                    _selectDirection = 0;
                  }
                  
                  break;
              }
            }
          }
          
          break;
      }
    }
  }
  
  private class KeyUpHandler implements ControlEvents.KeyEvent {
    @Override public void event(KeyEventData e) {
      switch(e.key) {
        case Keyboard.KEY_LSHIFT:
        case Keyboard.KEY_RSHIFT:
          _shift = false;
          break;
      }
    }
  }
  
  private class TextHandler implements ControlEvents.TextEvent {
    @Override public void event(TextEventData e) {
      updateText(_text[0] + e.key, EMPTY, _text[2]);
      _selectDirection = 0;
      events().onChange();
    }
  }
  
  private class FocusGotHandler implements ControlEvents.FocusEvent {
    @Override public void event(FocusEventData e) {
      _border.setColour(_hoverBorder);
      _border.createBorder();
      resetCaretAlpha();
    }
  }
  
  private class FocusLostHandler implements ControlEvents.FocusEvent {
    @Override public void event(FocusEventData e) {
      _border.setColour(_normalBorder);
      _border.createBorder();
    }
  }
  
  private class HoverInHandler implements ControlEvents.HoverEvent {
    @Override public void event(HoverEventData e) {
      _background.setTexture(_textureHover);
    }
  }
  
  private class HoverOutHandler implements ControlEvents.HoverEvent {
    @Override public void event(HoverEventData e) {
      _background.setTexture(_textureNormal);
    }
  }
  
  private boolean _down;
  private int _start;
  
  private class MouseMoveHandler implements ControlEvents.MouseEvent {
    @Override public void event(MouseEventData e) {
      if(_down) {
        int index = _font.regular().getCharAtX(_textFull.getText(), _mask, e.x - _padW);
        setSelLength(index - _start);
      }
    }
  }
  
  private class MouseDownHandler implements ControlEvents.MouseEvent {
    @Override public void event(MouseEventData e) {
      _down = true;
      _start = _font.regular().getCharAtX(_textFull.getText(), _mask, e.x - _padW);
      setSelStart(_start);
    }
  }
  
  private class MouseUpHandler implements ControlEvents.MouseEvent {
    @Override public void event(MouseEventData e) {
      _down = false;
    }
  }
  
  public static class Events extends ControlEvents {
    private Deque<ChangeEvent> _change = new ConcurrentLinkedDeque<>();
    
    public Events onChange(ChangeEvent e) { _change.add(e); return this; }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void onChange() {
      for(ChangeEvent e : _change) {
        e.event(new ChangeEventData(_control));
      }
    }
    
    public interface ChangeEvent extends Event<ChangeEventData> { }
    
    public class ChangeEventData extends EventData {
      private ChangeEventData(Control<? extends ControlEvents> control) {
        super(control);
      }
    }
  }
}
