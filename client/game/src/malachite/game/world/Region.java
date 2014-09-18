package malachite.game.world;

import malachite.game.Game;
import malachite.game.data.Map;
import malachite.gfx.Drawable;
import malachite.gfx.textures.Canvas;
import malachite.gfx.textures.Texture;

public class Region {
  private final Game _game;
  private final Map  _map;
  
  private final Drawable[] _layer = new Drawable[5];
  
  public Region(Game game, Map map) {
    _game = game;
    _map  = map;
  }
  
  public void calculate() {
    for(int i = 0; i < _map.layer.length; i++) {
      Map.Layer layer = _map.layer[i];
      
      Canvas canvas = _game.context.canvas()
        .name("test" + i).wh(512, 512).build();
      
      canvas.bind(() -> {
        for(int x = 0; x < layer.tile.length; x++) {
          for(int y = 0; y < layer.tile[x].length; y++) {
            Map.Tile tile = layer.tile[x][y];
            
            Texture t = _game.context.textures.getTexture("tiles/" + tile.set + ".png");
            
            Drawable d = _game.context.drawable()
              .texture(t)
              .xy(x * 32, y * 32).wh(32, 32)
              .st(tile.x * 32 / t.w, tile.y * 32 / t.h)
              .uv(32f / t.w, 32f / t.h)
              .buildQuad();
            
            d.draw();
          }
        }
      });
      
      _layer[i] = _game.context.drawable()
        .texture(canvas.texture)
        .autosize()
        .buildQuad();
    }
  }
  
  public void draw() {
    for(Drawable d : _layer) {
      if(d != null) {
        d.draw();
      }
    }
  }
}