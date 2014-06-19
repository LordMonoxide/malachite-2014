package net;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.codec.DecoderLength;
import net.codec.DecoderServer;
import net.codec.Encoder;
import net.codec.EncoderLength;
import net.codec.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
  private NioEventLoopGroup _parentGroup, _childGroup;
  private ServerBootstrap   _bootstrap;
  private Channel           _channel;
  private Connections       _connection = new Connections();
  public final Events        events     = new Events();
  
  public Server() {
    _connection = new Connections();
    
    _parentGroup = new NioEventLoopGroup();
    _childGroup  = new NioEventLoopGroup();
    _bootstrap = new ServerBootstrap()
      .group(_parentGroup, _childGroup)
      .channel(NioServerSocketChannel.class)
      .childHandler(new ChannelInitializer<SocketChannel>() {
        @Override protected void initChannel(SocketChannel ch) throws Exception {
          ch.pipeline().addLast(new EncoderLength(), new Encoder());
          ch.pipeline().addLast(new DecoderLength(), new DecoderServer(_connection));
          ch.pipeline().addLast(new Handler());
        }
      });
  }
  
  public void setPort(int port) {
    _bootstrap.localAddress(port);
  }
  
  public void setBacklog(int backlog) {
    _bootstrap.option(ChannelOption.SO_BACKLOG, new Integer(backlog));
  }
  
  public void setNoDelay(boolean noDelay) {
    _bootstrap.childOption(ChannelOption.TCP_NODELAY, new Boolean(noDelay));
  }
  
  public void setKeepAlive(boolean keepAlive) {
    _bootstrap.childOption(ChannelOption.SO_KEEPALIVE, new Boolean(keepAlive));
  }
  
  public void bind(Events.Event callback) {
    System.out.println("Binding...");
    
    _bootstrap.bind().addListener(new ChannelFutureListener() {
      @Override public void operationComplete(ChannelFuture future) throws Exception {
        System.out.println("Server bound and listening for connections.");
        
        _channel = future.channel();
        
        if(callback != null) {
          callback.event(future.isSuccess());
        }
      }
    });
  }
  
  public void close(Events.Event callback) {
    _channel.close().addListener(new ChannelFutureListener() {
      @Override public void operationComplete(ChannelFuture future) throws Exception {
        _channel = null;
        
        if(callback != null) {
          callback.event(future.isSuccess());
        }
      }
    });
  }
  
  public void shutdown() {
    _parentGroup.shutdownGracefully();
    _childGroup .shutdownGracefully();
  }
  
  public class Connections {
    private Map<Channel, Connection> _connection = new ConcurrentHashMap<>();
    
    private Connections() { }
    
    protected Connection add(Channel c) {
      Connection connection = _connection.get(c);
      
      if(connection == null) {
        connection = new Connection(c);
        _connection.put(c, connection);
      }
      
      return connection;
    }
    
    protected Connection remove(Channel c) {
      return _connection.remove(c);
    }
    
    public Connection get(Channel c) {
      return _connection.get(c);
    }
  }
  
  private class Handler extends SimpleChannelInboundHandler<Packet> {
    @Override public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      cause.printStackTrace();
      //throw new Exception(cause);
    }
    
    @Override public void channelActive(ChannelHandlerContext ctx) throws Exception {
      super.channelActive(ctx);
      Connection c = _connection.add(ctx.channel());
      
      if(c != null) {
        events.raiseConnect(c);
      }
    }
    
    @Override public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      super.channelInactive(ctx);
      events.raiseDisconnect(_connection.remove(ctx.channel()));
    }
    
    @Override protected void messageReceived(ChannelHandlerContext ctx, Packet msg) throws Exception {
      super.channelRead(ctx, msg);
      events.raisePacket(msg);
    }
  }
  
  public static final class Events {
    private List<Connect> _connect    = new LinkedList<>();
    private List<Connect> _disconnect = new LinkedList<>();
    private List<Packet>  _packet     = new LinkedList<>();
    
    public void onConnect   (Connect e) { _connect.add(e); }
    public void onDisconnect(Connect e) { _disconnect.add(e); }
    public void onPacket    (Packet  e) { _packet.add(e); }
    
    private Events() { }
    
    protected void raiseConnect(Connection c) {
      for(Connect e : _connect) {
        e.run(c);
      }
    }
    
    protected void raiseDisconnect(Connection c) {
      for(Connect e : _disconnect) {
        e.run(c);
      }
    }
    
    protected void raisePacket(net.codec.Packet p) {
      for(Packet e : _packet) {
        e.run(p);
      }
    }
    
    public interface Connect { public void run(Connection c); }
    public interface Packet  { public void run(net.codec.Packet p); }
    public interface Event   { public void event(boolean success); }
  }
}