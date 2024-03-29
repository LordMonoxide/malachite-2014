package malachite.gfx.gui.control;

import malachite.gfx.fonts.Font;
import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.fonts.TextStream;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.GUI;

public class Label extends Control<ControlEvents> {
  private Font _font = FontBuilder.getInstance().getDefault();
  private TextStream _textStream = new TextStream();
  private TextStream.Text _text = new TextStream.Text();
  private TextStream.Colour _textColour = new TextStream.Colour(65f / 255, 52f / 255, 8f / 255, 1);
  private float _textX, _textY;
  private int _textW, _textH;
  private boolean _autoSize;
  
  public Label() {
    this(InitFlags.WITH_DEFAULT_EVENTS);
  }
  
  public Label(InitFlags... flags) {
    super(flags);
    _textStream.insert(_textColour);
    _textStream.insert(_text);
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
  }
  
  public void setText(TextStream ts) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _textStream = ts;
      _textW = _font.regular().getW(ts);
      _textH = _font.regular().getH();
      
      if(_autoSize) {
        bounds.wh.set(_textW + _padW * 2 + 1, _textH + _padH * 2);
      }
    });
  }
  
  public void setText(String text) {
    _font.events().addLoadHandler(() -> {
      _needsUpdate = true;
      _text.setText(text);
      _textW = _font.regular().getW(text);
      _textH = _font.regular().getH();
      
      if(_autoSize) {
        bounds.wh.set(_textW + _padW * 2 + 1, _textH + _padH * 2);
      }
    });
  }

  public String getText() {
    return _text.getText();
  }
  
  public void setTextColour(float[] c) {
    _textColour.setColour(c);
  }
  
  public void setTextColour(float r, float g, float b, float a) {
    _textColour.setColour(r, g, b, a);
  }
  
  public float[] getTextColour() {
    return _textColour.getColour();
  }
  
  public boolean getAutoSize() {
    return _autoSize;
  }
  
  public void setAutoSize(boolean autoSize) {
    _autoSize = autoSize;
  }
  
  @Override protected void resize() {
    switch(_hAlign) {
      case ALIGN_LEFT:   _textX = _padW; break;
      case ALIGN_CENTER: _textX = (bounds.getW() - _textW) / 2; break;
      case ALIGN_RIGHT:  _textX =  bounds.getW() - _textW - _padW; break;
    }

    switch(_vAlign) {
      case ALIGN_TOP:    _textY = _padH; break;
      case ALIGN_MIDDLE: _textY = (bounds.getH() - _textH) / 2; break;
      case ALIGN_BOTTOM: _textY =  bounds.getH() - _textH - _padH; break;
    }
  }

  @Override public void draw() {
    if(drawBegin()) {
      _font.draw(_textX, _textY, (int)bounds.getW() - _padW * 2, (int)bounds.getH() - _padH * 2, _textStream);
    }

    drawEnd();
    drawNext();
  }
}
