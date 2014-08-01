package malachite.gfx.util;

public class Bounds {
  private boolean _maintainSize = true;
  private boolean _lock = false;
  
  public void maintainSize() { _maintainSize = true; }
  public void dontMaintainSize() { _maintainSize = false; }
  public boolean maintainsSize() { return _maintainSize; }
  
  public final Point xy = new Point() {
    @Override public void set(float x, float y) {
      super.set(x, y);
      updateSubpoints();
      updateXY();
    }
  };
  
  public final Point wh = new Point() {
    @Override public void set(float w, float h) {
      super.set(w, h);
      updateSubpoints();
      updateWH();
    }
  };
  
  public final Point tl = xy;
  
  public final Point tm = new Point() {
    @Override public void set(float x, float y) {
      super.set(x, y);
      
      if(_lock) { return; }
      
      if(_maintainSize) {
        xy.set(x - wh.getX() / 2, y);
      } else {
        
      }
    }
  };
  
  public final Point tr = new Point() {
    @Override public void set(float x, float y) {
      super.set(x, y);
      
      if(_lock) { return; }
      
      if(_maintainSize) {
        xy.set(x - wh.getX(), y);
      } else {
        
      }
    }
  };
  
  public final Point ml = new Point() {
    
  };
  
  public final Point mm = new Point() {
    
  };
  
  public final Point mr = new Point() {
    
  };
  
  public final Point bl = new Point() {
    
  };
  
  public final Point bm = new Point() {
    
  };
  
  public final Point br = new Point() {
    
  };
  
  private void updateSubpoints() {
    _lock = true;
    tr.set(xy.getX() + wh.getX(), xy.getY());
    tm.set(Points.mean(tl, tr));
    bl.set(xy.getX(), xy.getY() + wh.getY());
    br.set(tr.getX(), bl.getY());
    bm.set(tm.getX(), bl.getY());
    ml.set(Points.mean(tl, bl));
    mr.set(tr.getX(), ml.getY());
    mm.set(tm.getX(), ml.getY());
    _lock = false;
  }
  
  protected void updateXY() { }
  protected void updateWH() { }
  
  public float getX() { return xy.getX(); }
  public float getY() { return xy.getY(); }
  public float getW() { return wh.getX(); }
  public float getH() { return wh.getY(); }
  
  public void setX(float x) { xy.setX(x); }
  public void setY(float y) { xy.setY(y); }
  public void setW(float w) { wh.setX(w); }
  public void setH(float h) { wh.setY(h); }
  
  @Override public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("XY: ").append(xy).append('\n')
     .append("WH: ").append(wh).append('\n')
     .append("TL: ").append(tl).append('\n')
     .append("TM: ").append(tm).append('\n')
     .append("TR: ").append(tr).append('\n')
     .append("ML: ").append(ml).append('\n')
     .append("MM: ").append(mm).append('\n')
     .append("MR: ").append(mr).append('\n')
     .append("BL: ").append(bl).append('\n')
     .append("BM: ").append(bm).append('\n')
     .append("BR: ").append(br);
    return s.toString();
  }
}