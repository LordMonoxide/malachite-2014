package malachite.engine.net;

import malachite.engine.net.packets.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ConnectionHandler extends SimpleChannelInboundHandler<Packet> {
  private final Server _server;
  
  public ConnectionHandler(Server server) {
    _server = server;
  }
  
  @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    /*Connection c = _connection.add(ctx.channel());
    
    if(c != null) {
      _server.events.raiseConnect(c);
    }*/
  }
  
  @Override public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    //_server.events.raiseDisconnect(_connection.remove(ctx.channel()));
  }
  
  @Override protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
    _server.events.raisePacket(msg);
  }
}