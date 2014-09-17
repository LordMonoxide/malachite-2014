package malachite.game.world;

import malachite.game.Game;
import malachite.game.data.Map;
import malachite.gfx.Drawable;
import malachite.gfx.textures.Canvas;
import malachite.gfx.textures.Texture;

public class Region {
  private final Texture[] _layer = new Texture[5];
  
  public Region(Game game, Map map) {
    for(int i = 0; i < map.layer.length; i++) {
      Map.Layer layer = map.layer[i];
      
      Canvas canvas = game.context.canvas()
        .name("test").wh(512, 512).build();
      
      canvas.bind(() -> {
        for(int x = 0; x < layer.tile.length; x++) {
          for(int y = 0; y < layer.tile[x].length; y++) {
            Map.Tile tile = layer.tile[x][y];
            
            Texture t = game.context.textures.getTexture("tiles/" + tile.set + ".png");
            
            Drawable d = game.context.drawable()
              .texture(t)
              .xy(x * 32, y * 32).wh(32, 32)
              .st(tile.x * 32 / t.w, tile.y * 32 / t.h)
              .buildQuad();
            
            d.draw();
          }
        }
      });
      
      _layer[i] = canvas.texture;
    }
  }
}