package gfx.gui;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ControlEvents {
  private Deque<DrawEvent>    _draw      = new ConcurrentLinkedDeque<>();
  private Deque<MouseEvent>   _mouseMove = new ConcurrentLinkedDeque<>();
  private Deque<MouseEvent>   _mouseDown = new ConcurrentLinkedDeque<>();
  private Deque<MouseEvent>   _mouseUp   = new ConcurrentLinkedDeque<>();
  private Deque<ScrollEvent>  _scroll    = new ConcurrentLinkedDeque<>();
  private Deque<HoverEvent>   _hoverIn   = new ConcurrentLinkedDeque<>();
  private Deque<HoverEvent>   _hoverOut  = new ConcurrentLinkedDeque<>();
  private Deque<ClickEvent>   _click     = new ConcurrentLinkedDeque<>();
  private Deque<KeyEvent>     _keyDown   = new ConcurrentLinkedDeque<>();
  private Deque<KeyEvent>     _keyRepeat = new ConcurrentLinkedDeque<>();
  private Deque<KeyEvent>     _keyUp     = new ConcurrentLinkedDeque<>();
  private Deque<TextEvent>    _text      = new ConcurrentLinkedDeque<>();
  private Deque<FocusEvent>   _focusGot  = new ConcurrentLinkedDeque<>();
  private Deque<FocusEvent>   _focusLost = new ConcurrentLinkedDeque<>();
  private Deque<ResizeEvent>  _resize    = new ConcurrentLinkedDeque<>();
  private Deque<VisibleEvent> _show      = new ConcurrentLinkedDeque<>();
  private Deque<VisibleEvent> _hide      = new ConcurrentLinkedDeque<>();
  
  public ControlEvents onDraw     (DrawEvent    e) { _draw     .add(e); return this; }
  public ControlEvents onMouseMove(MouseEvent   e) { _mouseMove.add(e); return this; }
  public ControlEvents onMouseDown(MouseEvent   e) { _mouseDown.add(e); return this; }
  public ControlEvents onMouseUp  (MouseEvent   e) { _mouseUp  .add(e); return this; }
  public ControlEvents onScroll   (ScrollEvent  e) { _scroll   .add(e); return this; }
  public ControlEvents onHoverIn  (HoverEvent   e) { _hoverIn  .add(e); return this; }
  public ControlEvents onHoverOut (HoverEvent   e) { _hoverOut .add(e); return this; }
  public ControlEvents onClick    (ClickEvent   e) { _click    .add(e); return this; }
  public ControlEvents onKeyDown  (KeyEvent     e) { _keyDown  .add(e); return this; }
  public ControlEvents onKeyRepeat(KeyEvent     e) { _keyRepeat.add(e); return this; }
  public ControlEvents onKeyUp    (KeyEvent     e) { _keyUp    .add(e); return this; }
  public ControlEvents onText     (TextEvent    e) { _text     .add(e); return this; }
  public ControlEvents onFocusGot (FocusEvent   e) { _focusGot .add(e); return this; }
  public ControlEvents onFocusLost(FocusEvent   e) { _focusLost.add(e); return this; }
  public ControlEvents onResize   (ResizeEvent  e) { _resize   .add(e); return this; }
  public ControlEvents onShow     (VisibleEvent e) { _show     .add(e); return this; }
  public ControlEvents onHide     (VisibleEvent e) { _hide     .add(e); return this; }
  
  protected Control<? extends ControlEvents> _control;
  
  protected ControlEvents(Control<? extends ControlEvents> c) {
    _control = c;
  }
  
  protected void onDraw() {
    for(DrawEvent e : _draw) {
      e.event(new DrawEventData(_control));
    }
  }
  
  public void onMouseMove(int x, int y, int button) {
    for(MouseEvent e : _mouseMove) {
      e.event(new MouseEventData(_control, x, y, button));
    }
  }
  
  public void onMouseDown(int x, int y, int button) {
    for(MouseEvent e : _mouseDown) {
      e.event(new MouseEventData(_control, x, y, button));
    }
  }
  
  public void onMouseUp(int x, int y, int button) {
    for(MouseEvent e : _mouseUp) {
      e.event(new MouseEventData(_control, x, y, button));
    }
  }
  
  public void onScroll(int delta) {
    for(ScrollEvent e : _scroll) {
      e.event(new ScrollEventData(_control, delta));
    }
  }
  
  public void onHoverIn() {
    for(HoverEvent e : _hoverIn) {
      e.event(new HoverEventData(_control));
    }
  }
  
  public void onHoverOut() {
    for(HoverEvent e : _hoverOut) {
      e.event(new HoverEventData(_control));
    }
  }
  
  public void onClick() {
    for(ClickEvent e : _click) {
      e.event(new ClickEventData(_control));
    }
  }
  
  public void onKeyDown(int key) {
    for(KeyEvent e : _keyDown) {
      e.event(new KeyEventData(_control, key));
    }
  }
  
  public void onKeyRepeat(int key) {
    for(KeyEvent e : _keyRepeat) {
      e.event(new KeyEventData(_control, key));
    }
  }
  
  public void onKeyUp(int key) {
    for(KeyEvent e : _keyUp) {
      e.event(new KeyEventData(_control, key));
    }
  }
  
  public void onText(char key) {
    for(TextEvent e : _text) {
      e.event(new TextEventData(_control, key));
    }
  }
  
  public void onFocusGot() {
    for(FocusEvent e : _focusGot) {
      e.event(new FocusEventData(_control));
    }
  }
  
  public void onFocusLost() {
    for(FocusEvent e : _focusLost) {
      e.event(new FocusEventData(_control));
    }
  }
  
  public void onResize() {
    for(ResizeEvent e : _resize) {
      e.event(new ResizeEventData(_control));
    }
  }
  
  public void onShow() {
    for(VisibleEvent e : _show) {
      e.event(new VisibleEventData(_control));
    }
  }
  
  public void onHide() {
    for(VisibleEvent e : _hide) {
      e.event(new VisibleEventData(_control));
    }
  }
  
  protected interface Event <T extends EventData> {
    public void event(T data);
  }
  
  public interface DrawEvent    extends Event<DrawEventData>    { }
  public interface MouseEvent   extends Event<MouseEventData>   { }
  public interface ScrollEvent  extends Event<ScrollEventData>  { }
  public interface HoverEvent   extends Event<HoverEventData>   { }
  public interface ClickEvent   extends Event<ClickEventData>   { }
  public interface KeyEvent     extends Event<KeyEventData>     { }
  public interface TextEvent    extends Event<TextEventData>    { }
  public interface FocusEvent   extends Event<FocusEventData>   { }
  public interface ResizeEvent  extends Event<ResizeEventData>  { }
  public interface VisibleEvent extends Event<VisibleEventData> { }
  
  protected abstract class EventData {
    public final Control<? extends ControlEvents> control;
    
    protected EventData(Control<? extends ControlEvents> control) {
      this.control = control;
    }
  }
  
  public class DrawEventData extends EventData {
    private DrawEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
  
  public class MouseEventData extends EventData {
    public final int x, y, button;
    
    private MouseEventData(Control<? extends ControlEvents> control, int x, int y, int button) {
      super(control);
      this.x = x;
      this.y = y;
      this.button = button;
    }
  }
  
  public class ScrollEventData extends EventData {
    public final int delta;
    
    private ScrollEventData(Control<? extends ControlEvents> control, int delta) {
      super(control);
      this.delta = delta;
    }
  }
  
  public class HoverEventData extends EventData {
    private HoverEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
  
  public class ClickEventData extends EventData {
    private ClickEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
  
  public class KeyEventData extends EventData {
    public final int key;
    
    private KeyEventData(Control<? extends ControlEvents> control, int key) {
      super(control);
      this.key = key;
    }
  }
  
  public class TextEventData extends EventData {
    public final char key;
    
    private TextEventData(Control<? extends ControlEvents> control, char key) {
      super(control);
      this.key = key;
    }
  }
  
  public class FocusEventData extends EventData {
    private FocusEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
  
  public class ResizeEventData extends EventData {
    private ResizeEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
  
  public class VisibleEventData extends EventData {
    private VisibleEventData(Control<? extends ControlEvents> control) {
      super(control);
    }
  }
}