package malachite.gfx.shaders;

public interface Fragment {
  public String[] getUniforms();
  public String[] outputModifiers();
}