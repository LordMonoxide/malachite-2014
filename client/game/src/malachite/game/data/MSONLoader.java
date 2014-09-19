package malachite.game.data;

public class MSONLoader implements LoaderInterface {
  @Override public <T extends LoadableInterface> void request(TYPE type, String id, RequestResponse<T> callback) {
    switch(type) {
      case MAP:
        
        break;
    }
  }
  
  @Override public <T extends LoadableInterface> void store(T loadable, StoreResponse callback) {
    
  }
}