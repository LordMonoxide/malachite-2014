package malachite.gfx;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Program {
  private static Program _current;
  public static Program current() { return _current; }
  public static void clear() {
    if(_current != null) {
      _current = null;
      GL20.glUseProgram(0);
    }
  }
  
  protected int _id;
  
  public int id() { return _id; }
  
  public abstract boolean load(Shader vsh, Shader fsh);
  
  public void use() {
    if(_current != this) {
      _current = this;
      GL20.glUseProgram(_id);
      
      int error = GL11.glGetError();
      if(error != GL11.GL_NO_ERROR) {
        System.out.println("Error using shader " + error + ":\n" + getError());
      }
    }
  }
  
  public String getError() {
    int size = GL20.glGetProgrami(_id, GL20.GL_INFO_LOG_LENGTH);
    return GL20.glGetProgramInfoLog(_id, size);

  }
}