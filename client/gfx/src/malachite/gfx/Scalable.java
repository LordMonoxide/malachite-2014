package malachite.gfx;

import malachite.gfx.textures.Texture;

public abstract class Scalable extends Drawable {
  protected Scalable(Context ctx, Texture texture, Program program, float[] loc, boolean visible) {
    super(ctx, texture, program, loc, visible);
  }
  
  public abstract void setSize(float[] st, float[] sl, float tw, float th, float ts);
}