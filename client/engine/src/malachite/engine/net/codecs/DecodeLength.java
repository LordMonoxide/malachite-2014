package malachite.engine.net.codecs;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

public class DecodeLength extends ByteToMessageDecoder {
  @Override protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    if(in.readableBytes() < 4) { return; }
    
    in.markReaderIndex();
    int length = in.readInt();
    
    if(length < 0) {
      throw new CorruptedFrameException("Negative length: " + length); //$NON-NLS-1$
    }
    
    if(in.readableBytes() < length) {
      in.resetReaderIndex();
    } else {
      out.add(in.readBytes(length));
    }
  }
}