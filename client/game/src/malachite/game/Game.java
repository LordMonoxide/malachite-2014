package malachite.game;

import java.io.IOException;

import malachite.engine.Engine;
import malachite.engine.lang.Lang;
import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.ContextBuilder;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.GUIManager;

public abstract class Game {
  private Context _context;
  private GUI     _menu;
  
  protected final Engine engine;
  protected final Lang   lang;
  
  public Game() throws Exception {
    engine = new Engine();
    
    System.out.println(engine);
    
    lang = engine.providers.lang.lang().get();
    
    ContextBuilder.registerContext(malachite.gfx.gl21.Context.class);
    
    _context = ContextBuilder.create(lang, ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          try {
            _menu = createInitialGUI(ctx.GUIs());
          } catch(IOException e) {
            e.printStackTrace();
          }
          
          _menu.push();
        }
      });
    });
  }
  
  protected abstract GUI createInitialGUI(GUIManager guis) throws IOException;
  
  public void run() {
    _context.run();
  }
}