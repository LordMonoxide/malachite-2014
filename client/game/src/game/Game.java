package game;

import java.io.IOException;

import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;
import gfx.gui.GUI;

public class Game {
  private Context _context;
  private GUI     _menu;
  
  public void init() {
    Manager.registerContext(gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          try {
            _menu = ctx.GUIs().loadFromFile("mainmenu.json");
          } catch(IOException e) {
            e.printStackTrace();
          }
          
          //_menu = new MainMenu(new MainMenuProvider(Instances.newAuthGateway()));
          //_menu.showLogin();
          _menu.push();
        }
      });
    });
    
    _context.run();
  }
}