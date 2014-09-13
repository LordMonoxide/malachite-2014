package malachite.gfx;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ContextEvents {
  private final Deque<DrawEvent> _draw = new ConcurrentLinkedDeque<>();
  
  public ContextEvents onDraw(DrawEvent event) {
    _draw.push(event);
    return this;
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
  
  public interface DrawEvent extends Event<DrawEventData> { }
  
  public class DrawEventData implements EventData {
    public final Matrix matrix;
    
    public DrawEventData(Matrix matrix) {
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