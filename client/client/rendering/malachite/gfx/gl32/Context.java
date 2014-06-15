package malachite.gfx.gl32;

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
  
  @Override protected void createInstances() {
    malachite.gfx.Context._matrix = new Matrix();
    _matrix   = (Matrix)malachite.gfx.Context._matrix;
    _vertex   = Vertex.class;
    _drawable = Drawable.class;
  }
  
  @Override protected void updateSize() {
    _matrix.setProjection(getW(), getH());
  }
  
  @Override protected void cleanup() {
    ShaderBuilder.destroy();
  }
}