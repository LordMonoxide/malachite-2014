package gfx.gui.control;

import gfx.Context;
import gfx.Drawable;
import gfx.Scalable;
import gfx.fonts.TextStream;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.textures.TextureBuilder;

public class Tooltip extends Control<ControlEvents> {
  private Drawable _body, _tip;
  
  private Label _text;
  
  public Tooltip() {
    TextureBuilder t = TextureBuilder.getInstance();
    Scalable s = Context.newScalable();
    s.setTexture(t.getTexture("gui/title.png"));
    s.setSize(
        new float[] {2, 10, 2, 10},
        new float[] {2, 10, 2, 10},
        5, 21, 1
    );
    
    _body = s;
    _body.setX(7);
    _body.setH(21);
    _body.createQuad();
    
    _tip = Context.newDrawable();
    _tip.setTexture(t.getTexture("gui/tooltip.png"));
    _tip.createQuad();
    
    _text = new Label();
    _text.pos.setX(_body.getX() + 4);
    _text.setTextColour(1, 1, 1, 1);
    _text.setAutoSize(true);
    _text.events().onResize(e -> {
      size.setX(_text.pos.getX() + _text.pos.getX());
    });
    
    controls().add(_text);
    
    size.set(100, 21);
  }
  
  @Override protected void resize() {
    _body.setW(size.getX());
    _body.createQuad();
    
    _text.pos.setY((_body.getH() - _text.size.getY()) / 2);
  }
  
  public String getText() {
    return _text.getText();
  }
  
  public void setText(String text) {
    _text.setText(text);
  }
  
  public void setText(TextStream text) {
    _text.setText(text);
  }
  
  @Override public void draw() {
    if(drawBegin()) {
      _body.draw();
      _tip.draw();
    }
    
    drawEnd();
    drawNext();
  }
}