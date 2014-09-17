package malachite.engine.net;

import java.util.concurrent.atomic.AtomicBoolean;

import malachite.engine.net.codecs.DecodeLength;
import malachite.engine.net.codecs.DecodePacket;
import malachite.engine.net.codecs.EncodeLength;
import malachite.engine.net.codecs.EncodePacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
  public final ServerEvents events = new ServerEvents();
  
  private final EventLoopGroup  _boss, _work;
  private final ServerBootstrap _bootstrap;
  
  private Channel _channel;
  
  public Server() {
    Server _this = this;
    
    _boss = new NioEventLoopGroup();
    _work = new NioEventLoopGroup();
    
    _bootstrap = new ServerBootstrap()
      .group(_boss, _work)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override protected void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(
            new EncodeLength(), new EncodePacket(),
            new DecodeLength(), new DecodePacket(),
            new ConnectionHandler(_this)
          );
        }
      });
  }
  
  public void destroy() {
    AtomicBoolean done = new AtomicBoolean(false);
    
    _boss.shutdownGracefully().addListener(future -> {
      if(done.getAndSet(true)) {
        events.raiseDestroy();
      }
    });
    
    _work.shutdownGracefully().addListener(future -> {
      if(done.getAndSet(true)) {
        events.raiseDestroy();
      }
    });
  }
  
  public void bind(int port) {
    _bootstrap.bind(port).addListener(future -> {
      _channel = ((ChannelFuture)future).channel();
      events.raiseBind(future.isSuccess());
    });
  }
  
  public void close() {
    _channel.close().addListener(future -> {
      _channel = null;
      events.raiseClose(future.isSuccess());
    });
  }
}