package malachite.gfx;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL20;

public class ShaderBuilder {
  /* ----------------------------
   * -- TODO: Shader fragments --
   * ----------------------------
   * Allow shaders to be chained together via inputs/outputs
   */
  
  private final Map<String, Shader > _shader  = new HashMap<>();
  private final Map<String, Program> _program = new HashMap<>();
  
  private Shader getShader(String file, int type) {
    Shader s = _shader.get(file);
    if(s == null) {
      s = Context.newShader();
      if(!s.load(file, type)) {
        return null;
      }
    }
    
    return s;
  }
  
  public Program getProgram(String vertex, String fragment) {
    String file = vertex + '|' + fragment;
    
    Program prog = _program.get(file);
    
    if(prog == null) {
      Program p = Context.newProgram();
      
      Context.getContext().addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
        Shader vsh = getShader(vertex,   GL20.GL_VERTEX_SHADER);
        Shader fsh = getShader(fragment, GL20.GL_FRAGMENT_SHADER);
        p.load(vsh, fsh);
      });
      
      return p;
    }
    
    return prog;
  }
}