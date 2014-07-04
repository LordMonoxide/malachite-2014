package net;

import net.codec.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

public class Connection {
  public final Channel channel;
  
  public Connection(Channel channel) {
    this.channel = channel;
  }
  
  public void close() {
    try {
      channel.close().sync();
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }
  
  public void close(final Event callback) {
    channel.close().addListener(new ChannelFutureListener() {
      @Override public void operationComplete(ChannelFuture future) throws Exception {
        if(callback != null) {
          callback.event();
        }
      }
    });
  }
  
  public void kick(String reason) {
    System.err.println(channel.remoteAddress().toString() + " is being kicked: " + reason); //$NON-NLS-1$
    close(null);
  }
  
  public void send(Packet packet) {
    channel.write(packet);
  }
  
  public static interface Event {
    public void event();
  }
}