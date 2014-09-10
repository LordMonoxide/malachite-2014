package malachite.gfx;

import malachite.gfx.Context.VertexManager;

public abstract class Scalable extends Drawable {
  protected Scalable(VertexManager vm, Matrix matrix) {
    super(vm, matrix);
  }
  
  public abstract void setSize(float[] st, float[] sl, float tw, float th, float ts);
}