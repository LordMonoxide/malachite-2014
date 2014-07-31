package malachite.gfx.util;

public class Bounds {
  public final Point xy = new Point() {
    @Override public void set(float x, float y) {
      super.set(x, y);
      updateSubpoints();
      updateXY();
    }
    
    @Override public void setX(float x) {
      set(x, _y);
    }
    
    @Override public void setY(float y) {
      set(_x, y);
    }
  };
  
  public final Point wh = new Point() {
    @Override public void set(float w, float h) {
      super.set(w, h);
      updateSubpoints();
      updateWH();
    }
    
    @Override public void setX(float w) {
      set(w, _y);
    }
    
    @Override public void setY(float h) {
      set(_x, h);
    }
  };
  
  public final Point tl = new Point() {
    
  };
  
  public final Point tm = new Point() {
    
  };
  
  public final Point tr = new Point() {
    
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
    tl.set(xy);
    tr.set(Points.add(xy, wh));
    tm.set(Points.mean(tl, tr));
    bl.set(xy._x, xy._y + wh._y);
    br.set(tr._x, bl._y);
    bm.set(tm._x, bl._y);
    ml.set(Points.mean(tl, bl));
    mr.set(tr._x, ml._y);
    mm.set(tm._x, ml._y);
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
}