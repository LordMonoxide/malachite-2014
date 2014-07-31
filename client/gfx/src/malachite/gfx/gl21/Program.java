package malachite.gfx.gl21;

import malachite.gfx.Shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Program extends malachite.gfx.Program {
  @Override public boolean load(Shader vsh, Shader fsh) {
    _id = GL20.glCreateProgram();
    
    GL20.glAttachShader(_id, vsh.id());
    GL20.glAttachShader(_id, fsh.id());
    GL20.glLinkProgram(_id);
    
    if(GL20.glGetProgrami(_id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
      System.err.println("Error linking shader:\n" + getError());
      return false;
    }
    
    return true;
  }
}