package net.codec;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class Encoder extends MessageToMessageEncoder<Packet> {
  @Override protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
    ByteBuf data = msg.serialize();
    int length = data != null ? data.readableBytes() : 0;
    ByteBuf b = Unpooled.buffer(length + 1);
    b.writeByte(msg.id());
    
    if(data != null) {
      b.writeBytes(data);
    }
    
    out.add(b);
  }
}