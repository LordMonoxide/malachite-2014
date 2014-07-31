package malachite.game;

import java.io.IOException;

import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.GUIEvents;

public class Game {
  public static void main(String[] args) {
    new Game().init();
  }
  
  private Context _context;
  private GUI     _menu;
  
  public void init() {
    Manager.registerContext(malachite.gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          try {
            _menu = ctx.GUIs().loadFromFile("mainmenu.json", new GUIEvents() {
              public void login(String email, String password) {
                System.out.println(email);
                System.out.println(password);
              }
            });
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