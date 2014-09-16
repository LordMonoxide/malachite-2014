package malachite.gfx;

import java.util.HashMap;
import java.util.Map;

import malachite.gfx.shaders.Uniform;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;
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
  
  public void addUniform(Uniform uniform) {
    _uniforms.put(uniform.name, uniform);
    uniform.bind(this);
  }
  
  public <T> void setUniform(int id, T value) {
    if(value instanceof Float) {
      GL20.glUniform1f(id, (float)value);
    } else if(value instanceof Vector4f) {
      Vector4f vec = (Vector4f)value;
      GL20.glUniform4f(id, vec.x, vec.y, vec.z, vec.w);
    }
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