package malachite.gfx.gl32;

import malachite.gfx.Scalable;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

public class Context extends malachite.gfx.Context {
  private Matrix _matrix;
  
  @Override protected void createDisplay() throws LWJGLException {
    ContextAttribs contextAttribs = new ContextAttribs(3, 2).withProfileCore(true).withForwardCompatible(true);
    PixelFormat pixelFormat = new PixelFormat();
    Display.create(pixelFormat, contextAttribs);
  }
  
  @Override protected Matrix createMatrix() {
    return new Matrix();
  }
  
  @Override public Drawable newDrawable() {
    return new Drawable(this, matrix);
  }
  
  @Override public Scalable newScalable() {
    //TODO return new Scalable(this, matrix);
    return null;
  }
  
  @Override protected Vertex newVertex() {
    return new Vertex();
  }
  
  @Override protected void updateSize() {
    _matrix.setProjection(getW(), getH());
  }
  
  @Override protected void cleanup() {
    ShaderBuilder.destroy();
  }
}