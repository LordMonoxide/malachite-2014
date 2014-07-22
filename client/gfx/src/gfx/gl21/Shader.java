package gfx.gl21;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class Shader extends gfx.Shader {
  @Override public boolean load(String file, int type) {
    StringBuilder src = new StringBuilder();
    File f = new File("gfx/shaders/legacy/" + file);
    
    try(BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String line;
      while((line = reader.readLine()) != null) {
        src.append(line).append('\n');
      }
    } catch(FileNotFoundException e) {
      System.err.println("Legacy shader " + file + " not found");
      e.printStackTrace();
      return false;
    } catch(IOException e) {
      System.err.println("Error loading legacy shader " + file);
      e.printStackTrace();
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
      System.err.println("Error compiling shader " + name + ":\n" + error);
      return false;
    }
    
    return true;
  }
}