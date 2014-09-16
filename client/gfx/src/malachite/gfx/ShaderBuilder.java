package malachite.gfx;

import java.util.ArrayList;
import java.util.List;

import malachite.gfx.shaders.Fragment;
import malachite.gfx.shaders.RecolourFragment;

public final class ShaderBuilder {
  private final Context _ctx;
  
  private final List<Fragment> _fragments = new ArrayList<>();
  
  protected ShaderBuilder(Context ctx) {
    _ctx = ctx;
  }
  
  public ShaderBuilder recolour() {
    _fragments.add(new RecolourFragment());
    return this;
  }
  
  public Shader build() {
    StringBuilder vsh = new StringBuilder()
      .append("#version 120\n")
      .append("void main(void) {\n")
        .append("gl_TexCoord[0] = gl_MultiTexCoord0;\n")
        .append("gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * gl_Vertex;\n")
      .append('}');
    
    StringBuilder fsh = new StringBuilder()
      .append("#version 120\n")
      .append("uniform sampler2D texture;\n");
    
    for(Fragment frag : _fragments) {
      for(String uniform : frag.getUniforms()) {
        fsh.append("uniform ").append(uniform).append(";\n");
      }
    }
    
    fsh.append("void main(void) {\n")
      .append("gl_FragColor = texture2D(texture, gl_TexCoord[0].st);\n");
    
    for(Fragment frag : _fragments) {
      for(String out : frag.outputModifiers()) {
        fsh.append("gl_FragColor = gl_FragColor ").append(out).append(";\n");
      }
    }
    
    fsh.append('}');
    
    System.out.println(vsh);
    System.out.println(fsh);
    
    return null;
  }
}