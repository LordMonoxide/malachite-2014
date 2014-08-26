package malachite.gfx.gl21;

import malachite.gfx.Shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program extends malachite.gfx.Program {
  private static final Logger logger = LoggerFactory.getLogger(Program.class);
  
  @Override public boolean load(Shader vsh, Shader fsh) {
    _id = GL20.glCreateProgram();
    
    GL20.glAttachShader(_id, vsh.id());
    GL20.glAttachShader(_id, fsh.id());
    GL20.glLinkProgram(_id);
    
    if(GL20.glGetProgrami(_id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
      logger.error("Error linking shader:\n{}", getError()); //$NON-NLS-1$
      return false;
    }
    
    return true;
  }
}