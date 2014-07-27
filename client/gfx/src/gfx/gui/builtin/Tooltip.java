package gfx.gui.builtin;

import gfx.fonts.TextStream;
import gfx.gui.Control;
import gfx.gui.GUI;
import gfx.gui.ControlEvents;

public class Tooltip extends GUI {
  private gfx.gui.control.Tooltip _tooltip;
  private Control<? extends ControlEvents> _anchor;
  private TextStream _text;
  
  public Tooltip(Control<? extends ControlEvents> anchor, TextStream text) {
    _anchor = anchor;
    _text   = text;
    ready();
  }
  
  public Tooltip(Control<? extends ControlEvents> anchor, String text) {
    this(anchor, new TextStream(text));
  }
  
  @Override protected void load() {
    _tooltip = new gfx.gui.control.Tooltip();
    _tooltip.setText(_text);
    controls().add(_tooltip);
    resize();
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    
  }
  
  @Override protected void draw() {
    _tooltip.pos.set(_anchor.calculateTotalX() + _anchor.size.getX() - 7, _anchor.calculateTotalY());
  }
  
  @Override protected boolean logic() {
    return false;
  }
  
  @Override protected boolean handleMouseDown (int x, int y, int button) { pop(); return false; }
  @Override protected boolean handleMouseUp   (int x, int y, int button) { pop(); return false; }
  @Override protected boolean handleMouseWheel(int delta)                { pop(); return false; }
  @Override protected boolean handleKeyDown   (int key, boolean repeat)  { pop(); return false; }
  @Override protected boolean handleKeyUp     (int key)  { pop(); return false; }
  @Override protected boolean handleCharDown  (char key) { pop(); return false; }
}