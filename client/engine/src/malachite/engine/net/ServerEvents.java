package malachite.engine.net;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import malachite.engine.net.packets.Packet;

public class ServerEvents {
  private final Deque<ServerEvents.BindEvent>    _bind    = new ConcurrentLinkedDeque<>();
  private final Deque<ServerEvents.CloseEvent>   _close   = new ConcurrentLinkedDeque<>();
  private final Deque<ServerEvents.DestroyEvent> _destroy = new ConcurrentLinkedDeque<>();
  private final Deque<ServerEvents.PacketEvent>  _packet  = new ConcurrentLinkedDeque<>();
  
  public ServerEvents onBind(ServerEvents.BindEvent event) {
    _bind.push(event);
    return this;
  }
  
  public ServerEvents onClose(ServerEvents.CloseEvent event) {
    _close.push(event);
    return this;
  }
  
  public ServerEvents onDestroy(ServerEvents.DestroyEvent event) {
    _destroy.push(event);
    return this;
  }
  
  public ServerEvents onPacket(ServerEvents.PacketEvent event) {
    _packet.push(event);
    return this;
  }
  
  void raiseBind(boolean success) {
    for(ServerEvents.BindEvent event : _bind) {
      BindEventData data = new BindEventData(success);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseClose(boolean success) {
    for(ServerEvents.CloseEvent event : _close) {
      CloseEventData data = new CloseEventData(success);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raiseDestroy() {
    for(ServerEvents.DestroyEvent event : _destroy) {
      DestroyEventData data = new DestroyEventData();
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  void raisePacket(Packet packet) {
    for(ServerEvents.PacketEvent event : _packet) {
      PacketEventData data = new PacketEventData(packet);
      
      try {
        event.event(data);
      } catch(Exception ex) {
        throw new CallbackException(event, data, ex);
      }
    }
  }
  
  public interface Event<T extends ServerEvents.EventData> {
    public void event(T data);
  }
  
  public interface EventData { }
  
  public interface BindEvent    extends ServerEvents.Event<BindEventData> { }
  public interface CloseEvent   extends ServerEvents.Event<CloseEventData> { }
  public interface DestroyEvent extends ServerEvents.Event<DestroyEventData> { }
  public interface PacketEvent  extends ServerEvents.Event<PacketEventData> { }
  
  public class BindEventData implements ServerEvents.EventData {
    public final boolean success;
    
    private BindEventData(boolean success) {
      this.success = success;
    }
  }
  
  public class CloseEventData implements ServerEvents.EventData {
    public final boolean success;
    
    private CloseEventData(boolean success) {
      this.success = success;
    }
  }
  
  public class DestroyEventData implements ServerEvents.EventData {
    private DestroyEventData() { }
  }
  
  public class PacketEventData implements ServerEvents.EventData {
    public final Packet packet;
    
    private PacketEventData(Packet packet) {
      this.packet = packet;
    }
  }
  
  public static class CallbackException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public CallbackException(ServerEvents.Event<?> event, ServerEvents.EventData data, Throwable cause) {
      super("An unhandled exception was thrown in an event!\n" + 
        "Event: " + event + '\n' +
        "Data:  " + data  + '\n' +
        "Stacktrace:", cause); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
    }
  }
}