package malachite.gfx.gl21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Shader extends malachite.gfx.Shader {
  private static final Logger logger = LoggerFactory.getLogger(Shader.class);
  
  @Override public boolean load(String file, int type) {
    StringBuilder src = new StringBuilder();
    File f = new File("gfx/shaders/legacy/" + file);
    
    try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String line;
      while((line = reader.readLine()) != null) {
        src.append(line).append('\n');
      }
    } catch(FileNotFoundException e) {
      logger.error("Legacy shader " + file + " not found", e); //$NON-NLS-1$ //$NON-NLS-2$
      return false;
    } catch(IOException e) {
      logger.error("Error loading legacy shader " + file, e); //$NON-NLS-1$
      return false;
    }
    
    return load(file, src.toString(), type);
  }
  
  @Override public boolean load(String name, String source, int type) {
    _id = GL20.glCreateShader(type);
    GL20.glShaderSource(_id, source);
    GL20.glCompileShader(_id);
    
    if(GL20.glGetShaderi(_id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
      int size = GL20.glGetShaderi(_id, GL20.GL_INFO_LOG_LENGTH);
      String error = GL20.glGetShaderInfoLog(_id, size);
      logger.error("Error compiling shader {}:\n{}", name, error); //$NON-NLS-1$
      return false;
    }
    
    return true;
  }
}