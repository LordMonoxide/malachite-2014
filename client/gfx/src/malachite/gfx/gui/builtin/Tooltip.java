package malachite.gfx.gui.builtin;

import malachite.gfx.fonts.TextStream;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.GUI;

public class Tooltip extends GUI {
  private malachite.gfx.gui.control.Tooltip _tooltip;
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
    _tooltip = new malachite.gfx.gui.control.Tooltip();
    _tooltip.setText(_text);
    controls().add(_tooltip);
    resize();
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    
  }
  
  @Override protected void draw() {
    _tooltip.bounds.xy.set(_anchor.calculateTotalX() + _anchor.bounds.getW() - 7, _anchor.calculateTotalY());
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