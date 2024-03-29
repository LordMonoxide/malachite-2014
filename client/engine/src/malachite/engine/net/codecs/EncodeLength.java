package malachite.engine.net.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class EncodeLength extends MessageToByteEncoder<ByteBuf> {
  @Override protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
    int length = msg.readableBytes();
    out.ensureWritable(length + 4);
    out.writeInt(length);
    out.writeBytes(msg, msg.readerIndex(), length);
  }
}