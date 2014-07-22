package net.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class DecoderClient extends MessageToMessageDecoder<ByteBuf> {
  @Override protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
    byte index = msg.readByte();
    
    try {
      Packet packet = Packet.create(index, msg);
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