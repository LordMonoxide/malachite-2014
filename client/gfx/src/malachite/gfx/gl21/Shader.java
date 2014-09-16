package malachite.gfx.gl21;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shader extends malachite.gfx.Shader {
  private static final Logger logger = LoggerFactory.getLogger(Shader.class);
  
  protected Shader(String source, int type) {
    super(GL20.glCreateShader(type));
    GL20.glShaderSource(id, source);
    GL20.glCompileShader(id);
    
    if(GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      int size = GL20.glGetShaderi(id, GL20.GL_INFO_LOG_LENGTH);
      String error = GL20.glGetShaderInfoLog(id, size);
      logger.error("Error compiling shader:\n{}", error); //$NON-NLS-1$
    }
  }
}