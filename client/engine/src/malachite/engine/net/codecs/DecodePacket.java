package malachite.engine.net.codecs;

import java.util.List;

import malachite.engine.net.packets.Packets;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

public class DecodePacket extends MessageToMessageDecoder<ByteBuf> {
  @Override protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    byte id = msg.readByte();
    
    //TODO: kick on exceptions
    out.add(Packets.create(id, msg));
  }
}