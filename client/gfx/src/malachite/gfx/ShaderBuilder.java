package malachite.gfx;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL20;

import malachite.gfx.shaders.DesaturateFragment;
import malachite.gfx.shaders.Fragment;
import malachite.gfx.shaders.RecolourFragment;
import malachite.gfx.shaders.Uniform;

public final class ShaderBuilder {
  private final Context _ctx;
  
  private final List<Fragment> _fragments = new ArrayList<>();
  
  ShaderBuilder(Context ctx) {
    _ctx = ctx;
  }
  
  public ShaderBuilder recolour() {
    _fragments.add(new RecolourFragment());
    return this;
  }
  
  public ShaderBuilder desaturate() {
    _fragments.add(new DesaturateFragment());
    return this;
  }
  
  public Program build() {
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
      for(Uniform uniform : frag.getUniforms()) {
        fsh.append(uniform).append('\n');
      }
    }
    
    fsh.append("void main(void) {\n")
      .append("gl_FragColor = texture2D(texture, gl_TexCoord[0].st);\n");
    
    for(Fragment frag : _fragments) {
      for(String out : frag.outputModifiers()) {
        fsh.append(out).append(";\n");
      }
    }
    
    fsh.append('}');
    
    System.out.println(fsh);
    
    Program program = _ctx.newProgram(_ctx.newShader(vsh.toString(), GL20.GL_VERTEX_SHADER), _ctx.newShader(fsh.toString(), GL20.GL_FRAGMENT_SHADER));
    
    for(Fragment frag : _fragments) {
      for(Uniform uniform : frag.getUniforms()) {
        program.addUniform(uniform);
      }
    }
    
    return program;
  }
}