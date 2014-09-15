package malachite.gfx;

import malachite.gfx.textures.Texture;

public abstract class Drawable {
  protected final Context _ctx;
  protected final Matrix  _matrix;
  
  protected final Texture _texture;
  protected final Program _program;
  
  protected final float[] _loc = {0, 0, 0, 0};
  protected final float[] _tex = {0, 0, 1, 1};
  protected final float[] _col = {1, 1, 1, 1};
  protected boolean _visible = true;
  
  protected int _renderMode;
  protected Vertex[] _vertex;
  
  protected Drawable(Context ctx, Texture texture, Program program, float[] loc, float[] tex, float[] col, boolean visible) {
    _ctx     = ctx;
    _matrix  = _ctx.matrix;
    _texture = texture;
    _program = program;
    _visible = visible;
    
    for(int i = 0; i < _loc.length; i++) { _loc[i] = loc[i]; }
    for(int i = 0; i < _tex.length; i++) { _tex[i] = tex[i]; }
    for(int i = 0; i < _col.length; i++) { _col[i] = col[i]; }
  }
  
  public boolean isVisible() {
    return _visible;
  }
  
  public void setVisible(boolean visible) {
    _visible = visible;
  }
  
  public void show() { setVisible(true ); }
  public void hide() { setVisible(false); }
  
  public abstract void createQuad();
  public abstract void createBorder();
  public abstract void createLine();
  public abstract void draw();
}