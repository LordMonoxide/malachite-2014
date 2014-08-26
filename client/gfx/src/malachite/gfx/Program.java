package malachite.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Program {
  private static final Logger logger = LoggerFactory.getLogger(Program.class);
  
  private static int _current;
  public static void clear() {
    if(_current != 0) {
      _current = 0;
      GL20.glUseProgram(0);
    }
  }
  
  protected int _id;
  
  public int id() { return _id; }
  
  public abstract boolean load(Shader vsh, Shader fsh);
  
  public void use() {
    if(_current != _id) {
      _current = _id;
      GL20.glUseProgram(_id);
      
      int error = GL11.glGetError();
      if(error != GL11.GL_NO_ERROR) {
        logger.error("Error using shader {}:\n{}", Integer.valueOf(error), getError()); //$NON-NLS-1$
      }
    }
  }
  
  public String getError() {
    int size = GL20.glGetProgrami(_id, GL20.GL_INFO_LOG_LENGTH);
    return GL20.glGetProgramInfoLog(_id, size);

  }
}