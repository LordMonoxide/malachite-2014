package malachite.gfx;

public class Vertex {
  protected float[] _loc = {0, 0};
  protected float[] _tex = {0, 0};
  protected float[] _col = {0, 0, 0, 0};
  
  protected Vertex() { }
  protected Vertex(float[] loc, float[] tex, float[] col) {
    set(loc, tex, col);
  }
  
  public void set(float[] loc, float[] tex, float[] col) {
    _loc = loc;
    _tex = tex;
    _col = col;
    
    if(_col == null) {
      _col = new float[4];
    }
  }
  
  public void use() { }
  
  public float[] getLoc() { return _loc; }
  public float[] getTex() { return _tex; }
  public float[] getCol() { return _col; }
  public void setLoc(float[] loc) { _loc = loc; }
  public void setTex(float[] tex) { _tex = tex; }
  public void setCol(float[] col) { _col = col; }
  
  public void setLoc(float x, float y) {
    _loc[0] = x;
    _loc[1] = y;
  }
  
  public void setTex(float x, float y) {
    _tex[0] = x;
    _tex[0] = y;
  }
}