package malachite.gfx;

import malachite.gfx.textures.Texture;

public final class DrawableBuilder {
  private final Context _ctx;
  
  private Texture _texture;
  private Program _program;
  
  private final float[] _loc = {0, 0, 0, 0};
  private final float[] _tex = {0, 0, 0, 0};
  private final float[] _col = {1, 1, 1, 1};
  
  private boolean _visible = true;
  
  protected DrawableBuilder(Context ctx) {
    _ctx = ctx;
  }
  
  public DrawableBuilder texture(Texture texture) {
    _texture = texture;
    return this;
  }
  
  public DrawableBuilder program(Program program) {
    _program = program;
    return this;
  }
  
  public DrawableBuilder xy(float x, float y) {
    _loc[0] = x; _loc[1] = y;
    return this;
  }
  
  public DrawableBuilder wh(float w, float h) {
    _loc[2] = w; _loc[3] = h;
    return this;
  }
  
  public DrawableBuilder st(float s, float t) {
    _tex[0] = s; _loc[1] = t;
    return this;
  }
  
  public DrawableBuilder uv(float u, float v) {
    _loc[2] = u; _loc[3] = v;
    return this;
  }
  
  public DrawableBuilder colour(float r, float g, float b, float a) {
    _col[0] = r; _col[1] = g; _col[2] = b; _col[3] = a;
    return this;
  }
  
  public DrawableBuilder visible(boolean visible) {
    _visible = visible;
    return this;
  }
  
  private Drawable build() {
    return _ctx.newDrawable(_texture, _program, _loc, _tex, _col, _visible);
  }
  
  public Drawable buildQuad() {
    Drawable d = build();
    d.createQuad();
    return d;
  }
  
  public Drawable buildBorder() {
    Drawable d = build();
    d.createBorder();
    return d;
  }
  
  public Drawable buildLine() {
    Drawable d = build();
    d.createLine();
    return d;
  }
}