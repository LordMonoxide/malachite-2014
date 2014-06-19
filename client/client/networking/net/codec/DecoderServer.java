package net.codec;

import java.util.List;

import net.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class DecoderServer extends MessageToMessageDecoder<ByteBuf> {
  private Server.Connections _connections;
  
  public DecoderServer(Server.Connections connections) {
    _connections = connections;
  }
  
  @Override protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
    byte index = msg.readByte();
    
    try {
      Packet packet = Packet.create(index, msg, _connections.get(ctx.channel()));
      out.add(packet);
    } catch(IndexOutOfBoundsException e) {
      //TODO: disconnect or do something here
      e.printStackTrace();
      return;
    } catch(Packet.NotEnoughDataException e) {
      e.printStackTrace();
      return;
    }
  }
}