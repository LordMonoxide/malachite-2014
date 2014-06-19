package net;

import java.util.Deque;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.codec.DecoderClient;
import net.codec.DecoderLength;
import net.codec.EncoderLength;
import net.codec.Encoder;
import net.codec.Packet;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
  private NioEventLoopGroup _group;
  private Bootstrap         _bootstrap;
  private Channel           _channel;
  public final Events        events = new Events();
  
  public Client() {
    _group = new NioEventLoopGroup();
    _bootstrap = new Bootstrap()
      .group(_group)
      .channel(NioSocketChannel.class)
      .handler(new ChannelInitializer<SocketChannel>() {
        @Override protected void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(new EncoderLength(), new Encoder());
          ch.pipeline().addLast(new DecoderLength(), new DecoderClient());
          ch.pipeline().addLast(new Handler());
        }
      });
  }
  
  public void setAddress(String host, int port) {
    _bootstrap.remoteAddress(host, port);
  }
  
  public void setTimeout(int timeout) {
    _bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout);
  }
  
  public void setNoDelay(boolean noDelay) {
    _bootstrap.option(ChannelOption.TCP_NODELAY, noDelay);
  }
  
  public void setKeepAlive(boolean keepAlive) {
    _bootstrap.option(ChannelOption.SO_KEEPALIVE, keepAlive);
  }
  
  public void connect(Events.Event callback) {
    _bootstrap.connect().addListener(new ChannelFutureListener() {
      public void operationComplete(ChannelFuture future) throws Exception {
        _channel = future.channel();
        
        if(callback != null) {
          callback.event(future.isSuccess());
        }
      }
    });
  }
  
  public void close(Events.Event callback) {
    if(_channel != null) {
      _channel.close().addListener(new ChannelFutureListener() {
        public void operationComplete(ChannelFuture future) throws Exception {
          _channel = null;
          
          if(callback != null) {
            callback.event(future.isSuccess());
          }
        }
      });
    }
  }
  
  public void shutdown() {
    _group.shutdownGracefully();
  }
  
  public void send(Packet packet) {
    _channel.write(packet);
  }
  
  private class Handler extends SimpleChannelInboundHandler<Packet> {
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      throw new Exception(cause);
    }
    
    public void messageReceived(ChannelHandlerContext ctx, Packet msg) throws Exception {
      events.raisePacket(msg);
    }
  }
  
  public static final class Events {
    private Deque<Packet> _packet = new ConcurrentLinkedDeque<>();
    
    public void onPacket(Packet e) {
      onPacket(e, false);
    }
    
    public void onPacket(Packet e, boolean first) {
      if(first) {
        _packet.addFirst(e);
      } else {
        _packet.add(e);
      }
    }
    
    public void removePacket(Packet e) {
      _packet.remove(e);
    }
    
    protected Events() { }
    
    protected void raisePacket(net.codec.Packet p) {
      Iterator<Packet> it = _packet.iterator();
      while(it.hasNext()) {
        if(it.next().run(p, it)) {
          break;
        }
      }
    }
    
    public interface Packet { public boolean run  (net.codec.Packet p, Iterator<Packet> it); }
    public interface Event  { public void    event(boolean success); }
  }
}