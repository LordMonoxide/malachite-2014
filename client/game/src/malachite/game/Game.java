package malachite.game;

import malachite.engine.Engine;
import malachite.engine.lang.Lang;
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
    context.events.onDraw(ev -> {
      
    });
  }
  
  public void run() {
    context.run();
  }
}