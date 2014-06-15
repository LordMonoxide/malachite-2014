package malachite.client.gfx;

import malachite.api.Settings;
import malachite.engine.data.Map;
import malachite.engine.world.Region;
import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.Loader;
import malachite.gfx.Matrix;
import malachite.gfx.textures.Canvas;
import malachite.gfx.textures.Texture;
import malachite.gfx.textures.TextureBuilder;

public class RegionRenderer {
  private Matrix _matrix = Context.getMatrix();
  
  public final Region region;
  
  private Drawable[] _layer = new Drawable[Settings.Map.Depth];
  
  public RegionRenderer(Region region) {
    this.region = region;
    
    Context.getContext().addLoadCallback(Loader.LoaderThread.GRAPHICS, () -> {
      calc();
    });
  }
  
  private void calc() {
    for(int z = 0; z < Settings.Map.Depth; z++) {
      Texture  texture = createTextureFromLayer(z);
      Drawable layer   = null;
      
      if(texture != null) {
        layer = Context.newDrawable();
        layer.setTexture(texture);
        layer.createQuad();
      }
      
      _layer[z] = layer;
    }
  }
  
  public Texture createTextureFromLayer(int z) {
    final Drawable[] tiles = createDrawablesFromLayer(z);
    
    if(tiles != null) {
      Canvas c = new Canvas(region.x + "x" + region.y + "x" + z, Settings.Map.Size, Settings.Map.Size);
      c.events().addLoadHandler(() -> {
        c.bind(() -> {
          for(Drawable d : tiles) {
            d.draw();
          }
        });
      });
      
      return c.getTexture();
    }
    
    return null;
  }
  
  public Drawable[] createDrawablesFromLayer(final int z) {
    Drawable[] d = new Drawable[Settings.Map.Tile.Count * Settings.Map.Tile.Count];
    int tiles = 0;
    
    TextureBuilder t = TextureBuilder.getInstance();
    
    Map m = region.map;
    
    for(int x = 0; x < m.layer[z].tile.length; x++) {
      for(int y = 0; y < m.layer[z].tile[x].length; y++) {
        if(m.layer[z].tile[x][y].a != 0) {
          Drawable tile = Context.newDrawable();
          tile.setTexture(t.getTexture("tiles/" + m.layer[z].tile[x][y].tileset + ".png")); //$NON-NLS-1$ //$NON-NLS-2$
          tile.setXYWH(x * Settings.Map.Tile.Size, y * Settings.Map.Tile.Size, Settings.Map.Tile.Size, Settings.Map.Tile.Size);
          tile.setTXYWH(m.layer[z].tile[x][y].x * Settings.Map.Tile.Size, m.layer[z].tile[x][y].y * Settings.Map.Tile.Size, Settings.Map.Tile.Size, Settings.Map.Tile.Size);
          tile.createQuad();
          d[tiles++] = tile;
        }
      }
    }
    
    if(tiles != 0) {
      Drawable[] d2 = new Drawable[tiles];
      System.arraycopy(d, 0, d2, 0, tiles);
      return d2;
    }
    
    return null;
  }
  
  public void draw(int z) {
    _matrix.push();
    _matrix.translate(region.x, region.y);
    
    if(_layer[z] != null) {
      _layer[z].draw();
    }
    
    _matrix.pop();
  }
}