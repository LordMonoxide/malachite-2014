package malachite.gfx.gl14;

import org.lwjgl.opengl.GL11;

public final class Matrix extends malachite.gfx.Matrix {
  Matrix() { }
  
  @Override public void setProjection(int w, int h) { setProjection(w, h, false); }
  @Override public void setProjection(int w, int h, boolean flip) {
    GL11.glMatrixMode(GL11.GL_PROJECTION);
    GL11.glLoadIdentity();
    
    if(!flip) GL11.glOrtho(0, w, h, 0, 1, -1);
    else      GL11.glOrtho(0, w, 0, h, 1, -1);
    
    GL11.glMatrixMode(GL11.GL_MODELVIEW);
  }
  
  @Override public void push() {
    GL11.glPushMatrix();
  }
  
  @Override public void pop() {
    GL11.glPopMatrix();
  }
  
  @Override public void translate(float x, float y) {
    GL11.glTranslatef(x, y, 0);
  }
  
  @Override public void rotate(float angle, float x, float y) {
    GL11.glRotatef(angle, x, y, 0);
  }
  
  @Override public void scale(float x, float y) {
    GL11.glScalef(x, y, 1);
  }
  
  @Override public void reset() {
    GL11.glLoadIdentity();
  }
}