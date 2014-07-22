package gfx.textures;

//TODO: NOT YET SUPPORTED BY 3.2+

import gfx.Context;
import gfx.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;

public class Canvas {
  private Context _context = Context.getContext();
  private Matrix  _matrix  = Context.getMatrix();
  private Texture _texture;
  
  private int _id;
  
  public Canvas(String name, int w, int h) {
    IntBuffer buffer = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
    EXTFramebufferObject.glGenFramebuffersEXT(buffer);
    
    _id = buffer.get();
    _texture = TextureBuilder.getInstance().getTexture(name, w, h, null);
    
    bind();
    EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, _texture.getID(), 0);
    unbind();
  }
  
  public Texture getTexture() {
    return _texture;
  }
  
  public void clear() {
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
  }
  
  public void bind(Event e) {
    bind();
    e.run();
    unbind();
  }
  
  public void bind() {
    EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, _id);
    //GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
    GL11.glViewport(0, 0, _texture.getW(), _texture.getH());
    _matrix.setProjection(_texture.getW(), _texture.getH(), true);
    _matrix.push();
    _matrix.reset();
    clear();
  }
  
  public void unbind() {
    //GL11.glPopAttrib();
    _matrix.pop();
    _matrix.setProjection(_context.getW(), _context.getH(), false);
    GL11.glViewport(0, 0, _context.getW(), _context.getH());
    EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
  }
    
  public interface Event {
    void run();
  }
}