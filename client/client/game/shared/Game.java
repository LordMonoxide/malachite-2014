package shared;

import api.Lang;
import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;
import gfx.gui.GUI;
import net.http.Request;
import shared.gui.mainmenu.IMainMenu;
import shared.gui.mainmenu.IMainMenuProvider;
import shared.gui.mainmenu.MainMenu;

public class Game {
  private Context _context;
  
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }
  
  public void start() {
    Request.init();
    Lang.load();
    
    Manager.registerContext(gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setTitle(Lang.App.get(Lang.AppKeys.TITLE));
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          IMainMenuProvider provider = Instances.newMainMenuProvider();
          IMainMenu         menu     = new MainMenu(provider);
          
          provider.setMainMenu(menu);
          menu.showLogin();
          ((GUI)menu).push();
        }
        
        @Override public void onClosed() {
          Request.destroy();
        }
      });
    });
    
    _context.run();
  }
}