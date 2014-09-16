package malachite.gfx.shaders;

public interface Fragment {
  public Uniform[] getUniforms();
  public String [] outputModifiers();
}