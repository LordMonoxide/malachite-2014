package malachite.gfx;

import java.util.Objects;

import malachite.gfx.textures.Texture;

public final class DrawableBuilder {
  private final Context _ctx;
  
  private Texture _texture;
  private Program _program;
  
  private final float[] _loc  = {0, 0};
  private final float[] _size = {0, 0};
  private final float[] _tex  = {0, 0, 1, 1};
  private final float[] _col  = {1, 1, 1, 1};
  
  private boolean _visible = true;
  
  DrawableBuilder(Context ctx) {
    _ctx = ctx;
  }
  
  public DrawableBuilder texture(Texture texture) {
    _texture = Objects.requireNonNull(texture);
    return this;
  }
  
  public DrawableBuilder program(Program program) {
    _program = Objects.requireNonNull(program);
    return this;
  }
  
  public DrawableBuilder xy(float x, float y) {
    _loc[0] = x; _loc[1] = y;
    return this;
  }
  
  public DrawableBuilder wh(float w, float h) {
    _size[0] = w; _size[1] = h;
    return this;
  }
  
  public DrawableBuilder st(float s, float t) {
    _tex[0] = s; _tex[1] = t;
    return this;
  }
  
  public DrawableBuilder uv(float u, float v) {
    _tex[2] = u; _tex[3] = v;
    return this;
  }
  
  public DrawableBuilder colour(float r, float g, float b, float a) {
    _col[0] = r; _col[1] = g; _col[2] = b; _col[3] = a;
    return this;
  }
  
  public DrawableBuilder autosize() {
    wh(_texture.w, _texture.h);
    return this;
  }
  
  public DrawableBuilder visible(boolean visible) {
    _visible = visible;
    return this;
  }
  
  private Drawable build() {
    return _ctx.newDrawable(_texture, _program, _loc, _visible);
  }
  
  public Drawable buildQuad() {
    Drawable d = build();
    d.createQuad(_size, _tex, _col);
    return d;
  }
  
  public Drawable buildBorder() {
    Drawable d = build();
    d.createBorder(_size, _tex, _col);
    return d;
  }
}