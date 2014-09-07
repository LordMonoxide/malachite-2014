package malachite.gfx.gui.control;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.Scalable;
import malachite.gfx.gui.GUI;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.HAlign;

import org.lwjgl.input.Keyboard;

public class Button extends Label {
  private float[] _normalColour  = {0x3F / 255f, 0xCF / 255f, 0, 1};
  private float[] _hoverColour   = {0x46 / 255f, 0xE6 / 255f, 0, 1};
  private float[] _pressedColour = {0x28 / 255f, 0x82 / 255f, 0, 1};
  
  private float[] _normalBorder = {39f / 0xFF, 129f / 0xFF, 0f / 0xFF, 1};
  private float[] _hoverBorder  = { 7f / 0xFF,  85f / 0xFF, 0f / 0xFF, 1};
  
  private boolean _pressed;
  private boolean _hovered;
  
  public Button() {
    super(
      InitFlags.WITH_DEFAULT_EVENTS,
      InitFlags.ACCEPTS_FOCUS,
      InitFlags.REGISTER,
      InitFlags.WITH_BORDER
    );
    
    _events.onHoverIn(e -> {
      _hovered = true;
      
      if(!_pressed) {
        _background.setColour(_hoverColour);
        _background.createQuad();
      }
    }).onHoverOut(e -> {
      _hovered = false;
      
      if(!_pressed) {
        _background.setColour(_normalColour);
        _background.createQuad();
      }
    });
    
    _events.onMouseDown(e -> {
      _pressed = true;
      _background.setColour(_pressedColour);
      _background.createQuad();
    }).onMouseUp(e -> {
      _pressed = false;
      _background.setColour(_hovered ? _hoverColour : _normalColour);
      _background.createQuad();
    });
    
    _events.onFocusGot(e -> {
      _border.setColour(_hoverBorder);
      _border.createBorder();
    }).onFocusLost(e -> {
      _border.setColour(_normalBorder);
      _border.createBorder();
    });
    
    _events.onKeyDown(e -> {
      switch(e.key) {
        case Keyboard.KEY_RETURN:
        case Keyboard.KEY_SPACE:
          _events.onMouseDown(0, 0, -1);
          break;
      }
    }).onKeyUp(e -> {
      switch(e.key) {
        case Keyboard.KEY_RETURN:
        case Keyboard.KEY_SPACE:
          _events.onMouseUp(0, 0, -1);
          _events.onClick();
          break;
      }
    });
    
    _hAlign = HAlign.ALIGN_CENTER;
    
    Scalable s = Context.newScalable();
    s.setTexture(TextureBuilder.getInstance().getTexture("gui/button.png"));
    s.setSize(new float[] {2, 2, 2, 2},
      new float[] {2, 2, 2, 2},
      5, 5, 1
    );
    
    _background = s;
    
    setBackgroundColour(_normalColour);
    setTextColour(1, 1, 1, 1);
    _border.setColour(_normalBorder);
  }
  
  @Override public void setBackground(Drawable d) {
    super.setBackground(d);
    _normalColour = d.getColour();
    
    for(int i = 0; i < 2; i++) {
      _hoverColour[i]   = _normalColour[i] + _normalColour[i] * (1 - _normalColour[i]);
      _pressedColour[i] = _normalColour[i] + _normalColour[i] * (_normalColour[i] - 1);
    }
    
    _hoverColour[3]   = _normalColour[3];
    _pressedColour[3] = _normalColour[3];
  }
  
  @Override public void setBackgroundColour(float r, float g, float b, float a) {
    setBackgroundColour(new float[] {r, g, b, a});
  }
  
  @Override public void setBackgroundColour(float[] c) {
    super.setBackgroundColour(c);
    _normalColour = c;
    
    for(int i = 0; i < 2; i++) {
      _hoverColour[i]   = _normalColour[i] + _normalColour[i] * (1 - _normalColour[i]);
      _pressedColour[i] = _normalColour[i] + _normalColour[i] * (_normalColour[i] - 1);
    }
    
    _hoverColour[3]   = _normalColour[3];
    _pressedColour[3] = _normalColour[3];
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }
  
  @Override protected void resize() {
    super.resize();
    
    _border.setXYWH(0, 0, bounds.getW(), bounds.getH());
    _border.createBorder();
  }
}
