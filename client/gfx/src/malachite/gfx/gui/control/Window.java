package malachite.gfx.gui.control;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.gfx.Context;
import malachite.gfx.Scalable;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.ControlList;
import malachite.gfx.gui.GUI;
import malachite.gfx.textures.Texture;
import malachite.gfx.textures.TextureBuilder;
import malachite.gfx.util.Bounds;

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
    _title.bounds.setY(-20);
    _title.bounds.wh.bindX(bounds.wh);
    _title.bounds.setH(21);
    _title.events().onMouseMove(e -> {
      if(e.button == 0) {
        bounds.xy.set(bounds.getX() + e.x - _x, bounds.getY() + e.y - _y);
      }
    }).onMouseDown(e -> {
      _x = e.x;
      _y = e.y;
    });
    
    _text = new Label();
    _text.setTextColour(1, 1, 1, 1);
    _text.setAutoSize(true);
    _text.bounds.setX(4);
    _text.events().onResize(e -> {
      _text.bounds.setY((_title.bounds.getH() - _text.bounds.getH()) / 2);
    });
    
    _icon = new Image();
    _icon.bounds.xy.bindY(_title.bounds.wh);
    
    _title.controls().add(_text);
    _title.controls().add(_icon);
    
    _close = new Button();
    _close.setBackground(Context.newDrawable());
    _close.getBackground().setTexture(t.getTexture("gui/close.png"));
    _close.getBackground().setTWH(13, 13);
    _close.setBackgroundColour(new float[] {0.8f, 0.8f, 0.8f, 1});
    _close.bounds.wh.set(13, 13);
    _close.bounds.xy.bindX(_title.bounds.wh);
    _close.bounds.setY(4);
    _close.bounds.setX(-_close.bounds.getW() - _close.bounds.getY());
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
    _content.bounds.xy.set(8, 8);
    _content.bounds.wh.bind(bounds.wh);
    _content.bounds.wh.set(-16, -16);
    
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
      _icon.bounds.setY(-_icon.bounds.getH());
      _text.bounds.xy.bindX(_icon.bounds.wh);
      _text.bounds.setX(0);
    } else {
      _text.bounds.xy.bindX(null);
      _text.bounds.setX(4);
    }
  }
  
  public Bounds getContentBounds() {
    return _content.bounds;
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
