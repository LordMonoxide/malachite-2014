package malachite.game;

import malachite.engine.Engine;
import malachite.engine.lang.Lang;
import malachite.gfx.Context;
import malachite.gfx.ContextBuilder;

public abstract class Game {
  private Context _context;
  
  protected final Engine engine;
  protected final Lang   lang;
  
  public Game() throws Exception {
    engine = new Engine();
    
    System.out.println(engine);
    
    lang = engine.providers.lang.lang().get();
    
    _context = new ContextBuilder().registerContext(malachite.gfx.gl21.Context.class).build();
    _context.events.onDraw(ev -> {
      
    });
  }
  
  public void run() {
    _context.run();
  }
}