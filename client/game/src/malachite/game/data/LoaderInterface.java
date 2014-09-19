package malachite.game.data;

public interface LoaderInterface {
  public <T extends LoadableInterface> void request(TYPE type, String id, RequestResponse<T> callback);
  public <T extends LoadableInterface> void store  (T loadable, StoreResponse callback);
  
  public enum TYPE {
    MAP;
  }
  
  public interface RequestResponse<T> {
    public void success(T data);
    public void failure();
  }
  
  public interface StoreResponse {
    public void success();
    public void failure();
  }
}