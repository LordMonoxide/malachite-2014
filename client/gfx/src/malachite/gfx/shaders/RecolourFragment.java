package malachite.gfx.shaders;

public class RecolourFragment implements Fragment {
  @Override public String[] getUniforms() {
    return new String[] {"vec4 recolour"};
  }
  
  @Override public String[] outputModifiers() {
    return new String[] {"* recolour"};
  }
}