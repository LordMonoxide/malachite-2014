package malachite.client.game;

import malachite.api.Lang;
import malachite.api.Settings;
import malachite.client.gui.GameGUI;
import malachite.engine.world.Entity;
import malachite.engine.world.World;
import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.net.http.Request;

public final class Game {
  public static void main(String... args) {
    new Game().start();
  }
  
  private Context _context;
  
  private World  _world;
  private Entity _me;
  
  public void start() {
    Request.init();
    Lang.load();
    
    //Manager.registerContext(malachite.gfx.gl32.Context.class);
    Manager.registerContext(malachite.gfx.gl21.Context.class);
    
    _context = Manager.create(ctx -> {
      ctx.setTitle(Lang.App.get(Lang.AppKeys.TITLE));
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          _world = new World("Test");
          
          _me = new Entity(0, _world);
          _me.setXYZ(Settings.Map.Size / 2, Settings.Map.Size / 2, Settings.Map.Depth / 2);
          
          new GameGUI(new GameInterface()).push();
        }
        
        @Override
        public void onClosed() {
          Request.destroy();
        }
      });
    });
    
    _context.run();
  }
  
  public class GameInterface {
    public Entity me() {
      return _me;
    }
  }
}