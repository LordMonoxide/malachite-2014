package malachite.gfx.gl21;

import malachite.gfx.textures.Texture;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class Context extends malachite.gfx.Context {
  @Override protected void createDisplay() throws LWJGLException {
    ContextAttribs attribs = new ContextAttribs(2, 1);
    PixelFormat format = new PixelFormat();
    Display.create(format, attribs);
  }
  
  @Override protected Matrix createMatrix() {
    return new Matrix();
  }
  
  @Override protected Shader newShader(String source, int type) {
    return new Shader(source, type);
  }
  
  @Override protected Program newProgram(malachite.gfx.Shader vsh, malachite.gfx.Shader fsh) {
    return new Program(vsh, fsh);
  }
  
  @Override protected Drawable newDrawable(Texture texture, malachite.gfx.Program program, float[] loc, boolean visible) {
    return new Drawable(this, texture, program, loc, visible);
  }
  
  @Override protected Scalable newScalable(Texture texture, malachite.gfx.Program program, float[] loc, boolean visible) {
    return new Scalable(this, texture, program, loc, visible);
  }
  
  @Override protected Vertex newVertex() {
    return new Vertex();
  }
  
  @Override protected void updateSize() {
    GL11.glViewport(0, 0, getW(), getH());
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    GL11.glOrtho(0.0d, getW(), getH(), 0.0d, 1.0d, -1.0d);
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
  }
  
  @Override protected void cleanup() {
    
  }
}