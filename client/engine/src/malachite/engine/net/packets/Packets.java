package malachite.engine.net.packets;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public final class Packets {
  private Packets() { }
  
  private static final Map<Byte, Class<? extends Packet>> _packets = new HashMap<>();
  
  public static void register(byte id, Class<? extends Packet> packet) {
    _packets.put(id, packet);
  }
  
  public static Packet create(byte id, ByteBuf data) throws InstantiationException, IllegalAccessException {
    Packet packet = _packets.get(Byte.valueOf(id)).newInstance();
    packet.deserialize(data);
    return packet;
  }
}