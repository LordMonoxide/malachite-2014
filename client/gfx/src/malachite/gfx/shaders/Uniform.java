package malachite.gfx.shaders;

import malachite.gfx.Program;

public class Uniform {
  private int     _id;
  private Program _program;
  
  public final TYPE   type;
  public final String name;
  public final String value;
  
  Uniform(TYPE type, String name, String value) {
    this.type  = type;
    this.name  = name;
    this.value = value;
  }
  
  @Override public String toString() {
    StringBuilder s = new StringBuilder().append("uniform ").append(type.name).append(' ').append(name);
    
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
    _program.use();
    _program.setUniform(_id, value);
  }
  
  public enum TYPE {
    FLOAT("float"), VEC4("vec4");
    
    public final String name;
    
    TYPE(String name) {
      this.name = name;
    }
  }
}