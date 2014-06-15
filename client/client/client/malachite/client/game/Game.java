package malachite.client.game;

import malachite.api.Lang;
import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.net.http.Request;

public final class Game {
  public static void main(String... args) {
    new Game().start();
  }
  
  public void start() {
    Request.init();
    Lang.load();
    
    Manager.registerContext(malachite.gfx.gl32.Context.class);
    Manager.registerContext(malachite.gfx.gl14.Context.class);
    
    Context context = Manager.create(ctx -> {
      ctx.setTitle(Lang.App.get(Lang.AppKeys.TITLE));
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          
        }
        
        @Override
        public void onClosed() {
          Request.destroy();
        }
      });
    });
    
    context.run();
  }
}