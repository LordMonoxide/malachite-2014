package malachite.server;

import java.util.Scanner;

import net.Server;
import net.http.Request;
import malachite.api.Lang;
import malachite.api.Settings;
import malachite.client.gui.GameGUI;
import malachite.engine.Engine;
import malachite.engine.GameInterface;
import malachite.engine.world.Entity;
import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.server.data.Map;
import malachite.server.world.World;

public final class Game implements GameInterface {
  private Context _context;
  private Server  _server;
  
  private World  _world;
  private Entity _me;
  
  @Override public void start() {
    Engine.create(Map.class);
    
    Request.init();
    Lang.load();
    
    try(Scanner in = new Scanner(System.in)) {
      for(;;) {
        System.out.print("Start server? [yes]/no "); //$NON-NLS-1$
        String input = in.nextLine().toLowerCase();
        
        if("yes".startsWith(input)) { //$NON-NLS-1$
          _server = new Server();
          _server.setPort(4000);
          _server.setBacklog(100);
          _server.setKeepAlive(true);
          _server.setNoDelay(true);
          _server.bind(null);
          break;
        }
        
        if("no".startsWith(input)) { //$NON-NLS-1$
          break;
        }
      }
    }
    
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
    
    public void startMoving(float bear) {
      //_net.send(new EntityMoveStart(_entity.getX(), _entity.getY(), bear));
    }
    
    public void stopMoving() {
      //_net.send(new EntityMoveStop(_entity));
    }
  }
}