package malachite.gfx.shaders;

public class DesaturateFragment implements Fragment {
  @Override public Uniform[] getUniforms() {
    return new Uniform[] {
      new Uniform(Uniform.TYPE.FLOAT, "desat_amount", "1")
    };
  }
  
  @Override public String[] outputModifiers() {
    return new String[] {
      "vec3 lum = vec3(0.2125, 0.7154, 0.0721)",
      "float prod = dot(lum, gl_FragColor.rgb)",
      "gl_FragColor = mix(gl_FragColor, vec4(prod, prod, prod, gl_FragColor.a), desat_amount)"
    };
  }
}