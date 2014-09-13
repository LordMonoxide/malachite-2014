package malachite.gfx.textures;

import de.matthiasmann.twl.utils.PNGDecoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import malachite.gfx.util.Time;

public class TextureBuilder {
  private static final Logger logger = LoggerFactory.getLogger(TextureBuilder.class);
  
  private static final String TEXTURES_DIR = "../data/gfx/textures/"; //$NON-NLS-1$
  
  private final Map<String, Texture> _textures = new HashMap<>();
  
  public synchronized Texture getTexture(String name, int w, int h, ByteBuffer data) {
    double t = Time.get();
    
    if(_textures.containsKey(name)) {
      logger.trace("Texture \"{}\" already loaded.", name); //$NON-NLS-1$
      return _textures.get(name);
    }
    
    Texture texture = new Texture(name, w, h, data);
    _textures.put(name, texture);
    
    logger.info("Texture \"{}\" ({}x{}) loaded. ({}ms)", name, Integer.valueOf(w), Integer.valueOf(h), Double.valueOf(Time.get() - t)); //$NON-NLS-1$
    
    return texture;
  }
  
  public synchronized Texture getTexture(String file) {
    double t = Time.get();
    
    if(_textures.containsKey(file)) {
      logger.trace("Textures \"{}\" already loaded.", file); //$NON-NLS-1$
      return _textures.get(file);
    }
    
    ByteBuffer data = null;
    
    int w, h;
    
    try {
      File f = new File(TEXTURES_DIR + file);
      
      try(InputStream in = new FileInputStream(f)) {
        PNGDecoder png = new PNGDecoder(in);
        
        w = png.getWidth();
        h = png.getHeight();
        
        data = ByteBuffer.allocateDirect(4 * w * h);
        png.decode(data, w * 4, PNGDecoder.Format.RGBA);
        data.flip();
      }
    } catch(FileNotFoundException e) {
      logger.error("Couldn't find texture \"" + file + '\"', e); //$NON-NLS-1$
      return null;
    } catch(IOException e) {
      logger.error("Error loading texture \"" + file + '\"', e); //$NON-NLS-1$
      return null;
    }
    
    Texture texture = new Texture(file, w, h, data);
    _textures.put(file, texture);
    
    logger.info("Texture \"{}\" ({}x{}) loaded. ({}ms)", file, Integer.valueOf(w), Integer.valueOf(h), Double.valueOf(Time.get() - t)); //$NON-NLS-1$
    
    return texture;
  }
  
  public void destroy() {
    for(Texture t : _textures.values()) {
      t.destroy();
    }
    
    _textures.clear();
  }
}