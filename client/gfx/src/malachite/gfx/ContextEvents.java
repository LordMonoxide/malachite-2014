package malachite.gfx;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ContextEvents {
  private final Deque<LoadEvent>       _load       = new ConcurrentLinkedDeque<>();
  private final Deque<MouseMoveEvent>  _mouseMove  = new ConcurrentLinkedDeque<>();
  private final Deque<MouseDownEvent>  _mouseDown  = new ConcurrentLinkedDeque<>();
  private final Deque<MouseUpEvent>    _mouseUp    = new ConcurrentLinkedDeque<>();
  private final Deque<MouseWheelEvent> _mouseWheel = new ConcurrentLinkedDeque<>();
  private final Deque<DrawEvent>       _draw       = new ConcurrentLinkedDeque<>();
  
  public ContextEvents onLoad(LoadEvent event) {
    _load.push(event);
    return this;
  }
  
  public ContextEvents onMouseMove(MouseMoveEvent event) {
    _mouseMove.push(event);
    return this;
  }
  
  public ContextEvents onMouseDown(MouseDownEvent event) {
    _mouseDown.push(event);
    return this;
  }
  
  public ContextEvents onMouseUp(MouseUpEvent event) {
    _mouseUp.push(event);
    return this;
  }
  
  public ContextEvents onMouseWheel(MouseWheelEvent event) {
    _mouseWheel.push(event);
    return this;
  }
  
  public ContextEvents onDraw(DrawEvent event) {
    _draw.push(event);
    return this;
  }
  
  void raiseLoad() {
    for(LoadEvent event : _load) {
      LoadEventData data = new LoadEventData();
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseMouseMove(int x, int y) {
    for(MouseMoveEvent event : _mouseMove) {
      MouseMoveEventData data = new MouseMoveEventData(x, y);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseMouseDown(int x, int y, int button) {
    for(MouseDownEvent event : _mouseDown) {
      MouseButtonEventData data = new MouseButtonEventData(x, y, button);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseMouseUp(int x, int y, int button) {
    for(MouseUpEvent event : _mouseUp) {
      MouseButtonEventData data = new MouseButtonEventData(x, y, button);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseMouseWheel(int delta) {
    for(MouseWheelEvent event : _mouseWheel) {
      MouseWheelEventData data = new MouseWheelEventData(delta);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseDraw(Matrix matrix) {
    for(DrawEvent event : _draw) {
      DrawEventData data = new DrawEventData(matrix);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  public interface Event<T extends EventData> {
    public void event(T data) throws Exception;
  }
  
  public interface EventData { }
  
  public interface LoadEvent       extends Event<LoadEventData> { }
  public interface MouseMoveEvent  extends Event<MouseMoveEventData> { }
  public interface MouseDownEvent  extends Event<MouseButtonEventData> { }
  public interface MouseUpEvent    extends Event<MouseButtonEventData> { }
  public interface MouseWheelEvent extends Event<MouseWheelEventData> { }
  public interface DrawEvent       extends Event<DrawEventData> { }
  
  public class LoadEventData implements EventData {
    private LoadEventData() { }
  }
  
  public class MouseMoveEventData implements EventData {
    public final int x, y;
    
    private MouseMoveEventData(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
  
  public class MouseButtonEventData implements EventData {
    public final int x, y, button;
    
    private MouseButtonEventData(int x, int y, int button) {
      this.x      = x;
      this.y      = y;
      this.button = button;
    }
  }
  
  public class MouseWheelEventData implements EventData {
    public final int delta;
    
    private MouseWheelEventData(int delta) {
      this.delta = delta;
    }
  }
  
  public class DrawEventData implements EventData {
    public final Matrix matrix;
    
    private DrawEventData(Matrix matrix) {
      this.matrix = matrix;
    }
  }
  
  public static class CallbackException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CallbackException(Event<?> event, EventData data, Throwable cause) {
      super("An unhandled exception was thrown in an event!\n" + 
        "Event: " + event + '\n' +
        "Data:  " + data  + '\n' +
        "Stacktrace:", cause); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
  }
}