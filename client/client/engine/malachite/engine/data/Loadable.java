package malachite.engine.data;

public abstract class Loadable {
  private boolean _loaded;
  
  public synchronized void load(OnLoad onLoad) {
    if(!_loaded) {
      loadImpl();
      _loaded = true;
    }
    
    onLoad.loaded(this);
  }
  
  protected abstract void loadImpl();
  
  public interface OnLoad {
    public void loaded(Loadable l);
  }
}