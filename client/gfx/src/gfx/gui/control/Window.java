package gfx.gui.control;

import gfx.Context;
import gfx.Scalable;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.ControlList;
import gfx.gui.GUI;
import gfx.textures.Texture;
import gfx.textures.TextureBuilder;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Window<T extends Window.Events> extends Control<T> {
  private Image _title;
  private Label _text;
  private Image _icon;
  private Button _close;
  private Image _content;
  
  int _x, _y;
  
  public Window() {
    super(InitFlags.REGISTER);
    
    _events = new Events(this);
    
    TextureBuilder t = TextureBuilder.getInstance();
    
    Scalable s = Context.newScalable();
    s.setTexture(t.getTexture("gui/background.png"));
    s.setSize(
      new float[] {2, 2, 2, 2},
      new float[] {2, 2, 2, 2},
      260, 260, 256
    );
    
    _background = s;
    
    s = Context.newScalable();
    s.setTexture(t.getTexture("gui/title.png"));
    s.setSize(
      new float[] {2, 10, 2, 10},
      new float[] {2, 10, 2, 10},
      5, 21, 1
    );
    
    _title = new Image(InitFlags.WITH_DEFAULT_EVENTS, InitFlags.REGISTER);
    _title.setBackground(s);
    _title.pos.setY(-20);
    _title.size.bindX(size);
    _title.size.setY(21);
    _title.events().onMouseMove(e -> {
      if(e.button == 0) {
        pos.set(pos.getX() + e.x - _x, pos.getY() + e.y - _y);
      }
    }).onMouseDown(e -> {
      _x = e.x;
      _y = e.y;
    });
    
    _text = new Label();
    _text.setTextColour(1, 1, 1, 1);
    _text.setAutoSize(true);
    _text.pos.setX(4);
    _text.events().onResize(e -> {
      _text.pos.setY((_title.size.getY() - _text.size.getY()) / 2);
    });
    
    _icon = new Image();
    _icon.pos.bindY(_title.size);
    
    _title.controls().add(_text);
    _title.controls().add(_icon);
    
    _close = new Button();
    _close.setBackground(Context.newDrawable());
    _close.getBackground().setTexture(t.getTexture("gui/close.png"));
    _close.getBackground().setTWH(13, 13);
    _close.setBackgroundColour(new float[] {0.8f, 0.8f, 0.8f, 1});
    _close.size.set(13, 13);
    _close.pos.bindX(_title.size);
    _close.pos.setY(4);
    _close.pos.setX(-_close.size.getX() - _close.pos.getY());
    _close.events().onClick(e -> {
      events().onClose();
    });
    
    _title.controls().add(_close);
    
    s = Context.newScalable();
    s.setTexture(t.getTexture("gui/foreground.png"));
    s.setXY(-7, -7);
    s.setSize(
      new float[] {21, 21, 21, 21},
      new float[] {21, 21, 21, 21},
      43, 43, 1
    );
    
    _content = new Image();
    _content.setBackground(s);
    _content.pos.set(8, 8);
    _content.size.bind(size);
    _content.size.set(16, 16);
    
    super.controls().add(_title);
    super.controls().add(_content);
  }
  
  @Override protected void setGUI(GUI gui) {
    super.setGUI(gui);
    _title.setGUI(gui);
    _close.setGUI(gui);
    _content.setGUI(gui);
  }
  
  @Override public ControlList controls() {
    return _content.controls();
  }
  
  @Override protected void resize() {
    
  }
  
  public String getText() {
    return _text.getText();
  }
  
  public void setText(String text) {
    _text.setText(text);
  }
  
  public Texture getIcon() {
    return _icon.getTexture();
  }
  
  public void setIcon(Texture icon) {
    _icon.setTexture(icon);
    
    if(_icon != null) {
      _icon.pos.setY(-_icon.size.getY());
      _text.pos.bindX(_icon.size);
      _text.pos.setX(0);
    } else {
      _text.pos.bindX(null);
      _text.pos.setX(4);
    }
  }
  
  public float getContentW() {
    return _content.size.getX();
  }
  
  public float getContentH() {
    return _content.size.getY();
  }
  
  public void showCloseButton() {
    _close.show();
  }
  
  public void hideCloseButton() {
    _close.hide();
  }
  
  public static class Events extends ControlEvents {
    private Deque<CloseEvent> _close = new ConcurrentLinkedDeque<>();
    
    public Events onClose(CloseEvent e) { _close.add(e); return this; }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void onClose() {
      for(CloseEvent e : _close) {
        e.event(new CloseEventData(_control));
      }
    }
    
    public interface CloseEvent extends Event<CloseEventData> { }
    
    public class CloseEventData extends EventData {
      protected CloseEventData(Control<? extends ControlEvents> control) {
        super(control);
      }
    }
  }
}
