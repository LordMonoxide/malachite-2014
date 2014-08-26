package malachite.gfx.util;

public class Bounds {
  private HAlign _hAlign = HAlign.ALIGN_LEFT;
  private VAlign _vAlign = VAlign.ALIGN_TOP;
  
  private boolean _maintainSize = true;
  
  public void maintainSize() { _maintainSize = true; }
  public void dontMaintainSize() { _maintainSize = false; }
  public boolean maintainsSize() { return _maintainSize; }
  
  public HAlign getHAlign() { return _hAlign; }
  public VAlign getVAlign() { return _vAlign; }
  
  public void setHAlign(HAlign hAlign) { _hAlign = hAlign; }
  public void setVAlign(VAlign vAlign) { _vAlign = vAlign; }
  
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
    float x = 0, y = 0;
    switch(_hAlign) {
      case ALIGN_LEFT:   x = 0; break;
      case ALIGN_CENTER: x = -wh.getX() / 2; break;
      case ALIGN_RIGHT:  x = -wh.getX(); break;
    }
    
    switch(_vAlign) {
      case ALIGN_TOP:    y = 0; break;
      case ALIGN_MIDDLE: y = -wh.getY() / 2; break;
      case ALIGN_BOTTOM: y = -wh.getY(); break;
    }
    
    tl.set(xy.getX() + x, xy.getY() + y);
    tr.set(tl.getX() + wh.getX(), tl.getY());
    tm.set(Points.mean(tl, tr));
    bl.set(tl.getX(), tl.getY() + wh.getY());
    br.set(tr.getX(), bl.getY());
    bm.set(tm.getX(), bl.getY());
    ml.set(Points.mean(tl, bl));
    mr.set(tr.getX(), ml.getY());
    mm.set(tm.getX(), ml.getY());
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
    s.append("XY: ").append(xy).append('\n') //$NON-NLS-1$
     .append("WH: ").append(wh).append('\n') //$NON-NLS-1$
     .append("TL: ").append(tl).append('\n') //$NON-NLS-1$
     .append("TM: ").append(tm).append('\n') //$NON-NLS-1$
     .append("TR: ").append(tr).append('\n') //$NON-NLS-1$
     .append("ML: ").append(ml).append('\n') //$NON-NLS-1$
     .append("MM: ").append(mm).append('\n') //$NON-NLS-1$
     .append("MR: ").append(mr).append('\n') //$NON-NLS-1$
     .append("BL: ").append(bl).append('\n') //$NON-NLS-1$
     .append("BM: ").append(bm).append('\n') //$NON-NLS-1$
     .append("BR: ").append(br); //$NON-NLS-1$
    return s.toString();
  }
}