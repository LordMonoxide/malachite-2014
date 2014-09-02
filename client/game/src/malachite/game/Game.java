package malachite.game;

import java.io.IOException;

import malachite.engine.Engine;
import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.GUIManager;

public abstract class Game {
  private Context _context;
  private GUI     _menu;
  
  protected final Engine engine;
  
  public Game() {
    engine = new Engine();
    
    System.out.println(engine);
    
    Manager.registerContext(malachite.gfx.gl21.Context.class);
    
    _context = Manager.create(engine, ctx -> {
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