package malachite.game;

import malachite.engine.Engine;
import malachite.engine.lang.Lang;
import malachite.game.data.Map;
import malachite.game.world.Region;
import malachite.gfx.Context;
import malachite.gfx.ContextBuilder;

public abstract class Game {
  public final Context context;
  
  public final Engine engine;
  public final Lang   lang;
  
  public Game() throws Exception {
    engine = new Engine();
    
    System.out.println(engine);
    
    lang = engine.providers.lang.lang().get();
    
    Map map = new Map(0, 0);
    Region region = new Region(this, map);
    
    context = new ContextBuilder().registerContext(malachite.gfx.gl21.Context.class).build();
    context.events.onDraw(ev -> {
      region.draw();
    });
    
    context.threads.gfx(() -> {
      region.calculate();
    });
  }
  
  public void run() {
    context.run();
  }
}