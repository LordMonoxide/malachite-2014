package gfx.textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import util.Time;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class TextureBuilder {
  private static TextureBuilder _instance = new TextureBuilder();
  public static TextureBuilder getInstance() { return _instance; }
  
  private static final String TEXTURES_DIR = "gfx/textures/"; //$NON-NLS-1$

  private Map<String, Texture> _textures = new HashMap<>();
  
  public synchronized Texture getTexture(String name, int w, int h, ByteBuffer data) {
    double t = Time.get();

    if(_textures.containsKey(name)) {
      //System.out.println("Textures \"" + name + "\" already loaded."); //$NON-NLS-1$ //$NON-NLS-2$
      return _textures.get(name);
    }

    Texture texture = new Texture(name, w, h, data);
    _textures.put(name, texture);

    System.out.println("Texture \"" + name + "\" (" + w + 'x' + h + ") loaded. (" + (Time.get() - t) + ')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    return texture;
  }

  public synchronized Texture getTexture(String file) {
    double t = Time.get();

    if(_textures.containsKey(file)) {
      //System.out.println("Textures \"" + file + "\" already loaded."); //$NON-NLS-1$ //$NON-NLS-2$
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
      System.err.println("Couldn't find texture \"" + file + '\"'); //$NON-NLS-1$
      return null;
    } catch(IOException e) {
      System.err.println("Error loading texture \"" + file + '\"'); //$NON-NLS-1$
      e.printStackTrace();
      return null;
    }

    Texture texture = new Texture(file, w, h, data);
    _textures.put(file, texture);

    System.out.println("Texture \"" + file + "\" (" + w + 'x' + h +") loaded. (" + (Time.get() - t) + ')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    return texture;
  }

  public void destroy() {
    for(Texture t : _textures.values()) {
      t.destroy();
    }

    _textures.clear();
  }
}