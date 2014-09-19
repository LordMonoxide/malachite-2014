package malachite.game.world;

import malachite.gfx.Context;
import malachite.gfx.Drawable;
import malachite.gfx.util.Point;

public class Entity {
  public final int id;
  public final Point loc = new Point();
  
  private final Context _ctx;
  private final Drawable _drawable;
  
  public Entity(int id, Context ctx) {
    this.id = id;
    
    _ctx      = ctx;
    _drawable = ctx.drawable().wh(32, 32).buildQuad();
  }
  
  public void draw() {
    _ctx.matrix.push(() -> {
      _ctx.matrix.translate(loc);
      _drawable.draw();
    });
  }
}