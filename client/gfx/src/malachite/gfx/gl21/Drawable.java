package malachite.gfx.gl21;

import malachite.gfx.Program;
import malachite.gfx.Vertex;
import malachite.gfx.textures.Texture;

import org.lwjgl.opengl.GL11;

public class Drawable extends malachite.gfx.Drawable {
  protected int _renderMode;
  protected Vertex[] _vertex;
  
  protected Drawable(Context ctx, Texture texture, Program program, float[] loc, boolean visible) {
    super(ctx, texture, program, loc, visible);
  }
  
  @Override protected void createQuad(float[] size, float[] tex, float[] col) {
    _renderMode = GL11.GL_TRIANGLE_STRIP;
    _vertex = _ctx.vertices.createQuad(new float[] {0, 0, size[0], size[1]}, tex, col);
  }
  
  @Override protected void createBorder(float[] size, float[] tex, float[] col) {
    _renderMode = GL11.GL_LINE_STRIP;
    _vertex = _ctx.vertices.createBorder(new float[] {0, 0, size[0], size[1]}, col);
  }
  
  @Override public void draw() {
    if(_vertex == null || !_visible) { return; }
    
    _matrix.push();
    _matrix.translate(_loc[0], _loc[1]);
    
    if(_texture != null) {
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      _texture.use();
    } else {
      GL11.glDisable(GL11.GL_TEXTURE_2D);
    }
    
    if(_program != null) {
      _program.use();
    } else {
      malachite.gfx.Program.clear();
    }
    
    GL11.glBegin(_renderMode);
    
    for(Vertex vertex : _vertex) {
      if(vertex == null) { continue; }
      vertex.use();
    }
    
    GL11.glEnd();
    
    _matrix.pop();
  }
}