package malachite.gfx.gui.builtin;

import malachite.gfx.gui.GUI;
import malachite.gfx.gui.VAlign;
import malachite.gfx.gui.control.Label;
import malachite.gfx.gui.control.Window;

public class Message extends GUI {
  public static Message wait(String title, String text) {
    return new Message(title, text);
  }
  
  private String _initTitle;
  private String _initText;
  private Window<Window.Events> _wndMessage;
  private Label  _lblText;
  
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
    
  }

  @Override protected void draw() {
    
  }

  @Override protected boolean logic() {
    return false;
  }
}