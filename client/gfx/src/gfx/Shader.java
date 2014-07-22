package gfx;

public abstract class Shader {
  protected int _id;
  
  public int id() { return _id; }
  
  public abstract boolean load(String file, int type);
  public abstract boolean load(String name, String source, int type);
}