package game;

import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;

public class Game {
  private Context _context;
  
  public void init() {
    Manager.registerContext(gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          _menu = new MainMenu(new MainMenuProvider(Instances.newAuthGateway()));
          _menu.showLogin();
          ((GUI)_menu).push();
        }
      });
    });
    
    _context.run();
  }
}