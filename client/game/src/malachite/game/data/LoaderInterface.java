package malachite.game.data;

import io.netty.buffer.ByteBuf;

public interface LoaderInterface {
  public void request(LoadableInterface<?> loadable, Callback callback);
  
  public enum TYPE {
    MAP;
  }
  
  public interface Callback {
    public void success(ByteBuf data);
    public void failure();
  }
}