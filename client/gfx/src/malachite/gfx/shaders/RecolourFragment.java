package malachite.gfx.shaders;

public class RecolourFragment implements Fragment {
  @Override public String[] getUniforms() {
    return new String[] {"vec4 recolour = vec4(1, 1, 1, 1)"};
  }
  
  @Override public String[] outputModifiers() {
    return new String[] {"* recolour"};
  }
}