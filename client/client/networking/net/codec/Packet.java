package net.codec;

import java.util.ArrayList;
import java.util.List;

import net.Connection;
import io.netty.buffer.ByteBuf;

public abstract class Packet {
  private static List<Class<? extends Packet>> _packet = new ArrayList<>();
  
  public static void register(Class<? extends Packet> packet) {
    _packet.add(packet);
  }
  
  static Packet create(int index, ByteBuf data, Connection connection) throws IndexOutOfBoundsException, Packet.NotEnoughDataException {
    Class<? extends Packet> packet = _packet.get(index);
    
    try {
      Packet p = packet.newInstance();
      p._connection = connection;
      p.deserialize(data);
      return p;
    } catch(InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    
    return null;
  }
  
  protected Connection _connection;
  
  public abstract byte id();
  public abstract ByteBuf serialize();
  public abstract void deserialize(ByteBuf data) throws NotEnoughDataException;
  
  public static class NotEnoughDataException extends Exception {
    private static final long serialVersionUID = 1L;
    public NotEnoughDataException() {
      super("Not enough data"); //$NON-NLS-1$
    }
  }
}