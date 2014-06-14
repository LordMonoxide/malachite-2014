package malachite.gfx.gl32;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;

public class Context extends malachite.gfx.Context {
  @Override protected void createDisplay() throws LWJGLException {
    ContextAttribs contextAttribs = new ContextAttribs(3, 2).withProfileCore(true).withForwardCompatible(true);
    PixelFormat pixelFormat = new PixelFormat();
    Display.create(pixelFormat, contextAttribs);
  }
  
  @Override protected void createInstances() {
    _matrix   = new Matrix();
    _vertex   = Vertex.class;
    _drawable = Drawable.class;
  }
  
  @Override protected void updateSize() {
    
  }
  
  @Override protected void cleanup() {
    ShaderBuilder.destroy();
  }
}