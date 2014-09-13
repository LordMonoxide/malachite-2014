package malachite.gfx;

public abstract class Scalable extends Drawable {
  protected Scalable(Context ctx, Matrix matrix) {
    super(ctx, matrix);
  }
  
  public abstract void setSize(float[] st, float[] sl, float tw, float th, float ts);
}