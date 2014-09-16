package malachite.gfx.shaders;

import org.lwjgl.util.vector.Vector4f;

import malachite.gfx.Program;

public class Uniform {
  private int     _id;
  private Program _program;
  
  public final Type   type;
  public final String name;
  public final String value;
  
  Uniform(Type type, String name, String value) {
    this.type  = type;
    this.name  = name;
    this.value = value;
  }
  
  @Override public String toString() {
    StringBuilder s = new StringBuilder().append("uniform ").append(type.name()).append(' ').append(name);
    
    if(value != null) {
      s.append(" = ").append(value);
    }
    
    s.append(';');
    
    return s.toString();
  }
  
  //TODO package-protect
  public void bind(Program program) {
    _program = program;
    _id      = program.getUniformLocation(name);
  }
  
  //TODO package-protect
  public <T> void set(T value) {
    if(value instanceof Vector4f) {
      Vector4f vec = (Vector4f)value;
      _program.setUniform4f(_id, vec.x, vec.y, vec.z, vec.w);
    }
  }
  
  public enum Type {
    vec4;
  }
}