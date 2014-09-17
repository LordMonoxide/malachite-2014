package malachite.gfx.textures;

import org.lwjgl.opengl.GL11;

import java.nio.ByteBuffer;

public class Texture {
  @Override public String toString() {
    return "Texture " + id + ' ' + name + " (" + w + 'x' + h + ')'; //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  public final int id;
  public final String name;
  public final int w, h;
  
  protected Texture(String name, int w, int h, ByteBuffer data) {
    this.name = name;
    this.w = w;
    this.h = h;
    
    this.id = GL11.glGenTextures();
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, this.w, this.h, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
  }
  
  public void use() {
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, this.id);
  }
  
  public void destroy() {
    GL11.glDeleteTextures(this.id);
  }
}