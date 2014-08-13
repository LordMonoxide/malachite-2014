package malachite.game;

import java.io.IOException;
import java.util.Objects;

import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.parser.GUIEvents;

public class Game {
  private Context _context;
  private GUI     _menu;
  
  public void init(String gui, GUIEvents guiEventHandler) {
    Manager.registerContext(malachite.gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          try {
            _menu = ctx.GUIs().loadFromFile(gui, guiEventHandler);
          } catch(IOException e) {
            e.printStackTrace();
          }
          
          _menu.push();
        }
      });
    });
  }
  
  public void run() {
    Objects.requireNonNull(_context, this + " must be initialized before it is run.");
    
    _context.run();
  }
}