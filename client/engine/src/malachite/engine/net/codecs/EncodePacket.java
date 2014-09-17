package malachite.engine.net.codecs;

import java.util.List;

import malachite.engine.net.packets.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

public class EncodePacket extends MessageToMessageEncoder<Packet> {
  @Override protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
    //TODO: optimise?
    
    ByteBuf data = msg.serialize();
    ByteBuf b;
    
    if(data != null) {
      b = Unpooled.buffer(data.readableBytes() + 1);
      b.writeByte(msg.id);
      b.writeBytes(data);
    } else {
      b = Unpooled.buffer(1);
      b.writeByte(msg.id);
    }
    
    out.add(b);
  }
}