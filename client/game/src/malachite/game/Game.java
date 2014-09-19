package malachite.game;

import malachite.engine.Engine;
import malachite.engine.lang.Lang;
import malachite.game.data.MSONLoader;
import malachite.game.data.MSONMapSerializer;
import malachite.game.data.Map;
import malachite.game.world.Entity;
import malachite.game.world.Region;
import malachite.game.world.World;
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
    
    context = new ContextBuilder().registerContext(malachite.gfx.gl21.Context.class).build();
    
    Map map = new Map(0, 0);
    Region region = new Region(this, map);
    World world = new World();
    
    MSONMapSerializer ms = new MSONMapSerializer(map);
    System.out.println(ms.serialize().toString(2));
    
    context.events.onDraw(ev -> {
      region.draw();
      world.draw();
    });
    
    context.threads.gfx(() -> {
      region.calculate();
    });
    
    Entity me = new Entity(0, context);
    context.camera.bind(me.loc);
    
    world.entities.add(me);
  }
  
  public void run() {
    context.run();
  }
}