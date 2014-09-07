package malachite.gfx.gui.builtin;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.control.Label;
import malachite.gfx.gui.control.Window;
import malachite.gfx.util.VAlign;

public class Message extends GUI {
  public static Message wait(String title, String text) {
    return new Message(title, text);
  }
  
  private String _initTitle;
  private String _initText;
  private Window<Window.Events> _wndMessage;
  private Label  _lblText;
  
  private Drawable _background;
  private float[]  _backgroundColour = new float[] {0, 0, 0, 0};
  
  private Message(String initTitle, String initText) {
    _initTitle = initTitle;
    _initText  = initText;
    ready();
  }
  
  @Override protected void load() {
    _lblText = new Label();
    _lblText.setVAlign(VAlign.ALIGN_TOP);
    _lblText.setText(_initText);
    
    _wndMessage = new Window<>();
    _wndMessage.bounds.wh.set(300, 100);
    _wndMessage.bounds.xy.set((_context.getW() - _wndMessage.bounds.getW()) / 2, (_context.getH() - _wndMessage.bounds.getH()) / 2);
    _wndMessage.setText(_initTitle);
    _wndMessage.setIcon(_textures.getTexture("gui/icons/speech.png"));
    _wndMessage.hideCloseButton();
    _wndMessage.controls().add(_lblText);
    _wndMessage.events().onResize(e -> {
      _lblText.bounds.wh.set(_wndMessage.getContentBounds().getW() - _lblText.bounds.getX() * 2, _wndMessage.getContentBounds().getH() - _lblText.bounds.getY() * 2);
    });
    
    controls().add(_wndMessage);
    
    _background = Context.newDrawable();
    _background.setColour(_backgroundColour);
    
    resize();
  }
  
  public String getTitle() {
    return _wndMessage.getText();
  }
  
  public void setTitle(String title) {
    _wndMessage.setText(title);
  }
  
  public String getText() {
    return _lblText.getText();
  }
  
  public void setText(String text) {
    _lblText.setText(text);
  }

  @Override public void destroy() {
    
  }

  @Override protected void resize() {
    _background.setWH(_context.getW(), _context.getH());
    _background.createQuad();
  }

  @Override protected void draw() {
    _matrix.push(() -> {
      _matrix.reset();
      _background.draw();
    });
  }

  @Override protected boolean logic() {
    if(_backgroundColour[3] < 0.75f) {
      _backgroundColour[3] += 0.02f;
      _background.createQuad();
    }
    
    return false;
  }
  
  @Override protected boolean handleMouseDown (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseUp   (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseMove (int x, int y, int button) { return true; }
  @Override protected boolean handleMouseWheel(int delta)                { return true; }
  @Override protected boolean handleKeyDown   (int key, boolean repeat)  { return true; }
  @Override protected boolean handleKeyUp     (int key)  { return true; }
  @Override protected boolean handleCharDown  (char key) { return true; }
}