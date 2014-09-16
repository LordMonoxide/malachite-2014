package malachite.gfx;

import java.util.HashMap;
import java.util.Map;

import malachite.gfx.shaders.Uniform;

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
  
  public final int id;
  
  private final Map<String, Uniform> _uniforms = new HashMap<>();
  
  protected Program(int id) {
    this.id = id;
  }
  
  public Uniform getUniform(String name) {
    return _uniforms.get(name);
  }
  
  public int getUniformLocation(String name) {
    return GL20.glGetUniformLocation(id, name);
  }
  
  public void setUniform4f(int location, float v0, float v1, float v2, float v3) {
    GL20.glUniform4f(location, v0, v1, v2, v3);
  }
  
  public void addUniform(Uniform uniform) {
    _uniforms.put(uniform.name, uniform);
    uniform.bind(this);
  }
  
  public void use() {
    if(_current != id) {
      _current = id;
      GL20.glUseProgram(id);
      
      int error = GL11.glGetError();
      if(error != GL11.GL_NO_ERROR) {
        logger.error("Error using shader {}:\n{}", Integer.valueOf(error), getError()); //$NON-NLS-1$
      }
    }
  }
  
  public String getError() {
    int size = GL20.glGetProgrami(id, GL20.GL_INFO_LOG_LENGTH);
    return GL20.glGetProgramInfoLog(id, size);
  }
}