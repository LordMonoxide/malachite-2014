package malachite.gfx;

import malachite.gfx.textures.Canvas;

public class CanvasBuilder {
  private final Context _ctx;
  
  private String _name;
  private int _w, _h;
  
  CanvasBuilder(Context ctx) {
    _ctx = ctx;
  }
  
  public CanvasBuilder name(String name) {
    _name = name;
    return this;
  }
  
  public CanvasBuilder wh(int w, int h) {
    _w = w; _h = h;
    return this;
  }
  
  public Canvas build() {
    return new Canvas(_ctx, _ctx.matrix, _name, _w, _h);
  }
}