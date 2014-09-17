package malachite.engine.net.packets;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
  public final byte id;
  
  protected Packet(byte id) {
    this.id = id;
  }
  
  public abstract ByteBuf serialize();
  public abstract void deserialize(ByteBuf data);
}