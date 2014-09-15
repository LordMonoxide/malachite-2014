package malachite.gfx;

import malachite.gfx.textures.Texture;

public abstract class Drawable {
  protected final Context _ctx;
  protected final Matrix  _matrix;
  
  protected final Texture _texture;
  protected final Program _program;
  
  protected final float[] _loc = {0, 0};
  protected boolean _visible = true;
  
  protected Drawable(Context ctx, Texture texture, Program program, float[] loc, boolean visible) {
    _ctx     = ctx;
    _matrix  = _ctx.matrix;
    _texture = texture;
    _program = program;
    _visible = visible;
    
    for(int i = 0; i < _loc.length; i++) { _loc[i] = loc[i]; }
  }
  
  public boolean isVisible() {
    return _visible;
  }
  
  public void setVisible(boolean visible) {
    _visible = visible;
  }
  
  public void show() { setVisible(true ); }
  public void hide() { setVisible(false); }
  
  protected abstract void createQuad  (float[] size, float[] tex, float[] col);
  protected abstract void createBorder(float[] size, float[] tex, float[] col);
  
  public abstract void draw();
}