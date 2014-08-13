package malachite.gfx.test;

import malachite.gfx.Context;
import malachite.gfx.ContextListenerAdapter;
import malachite.gfx.Manager;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.parser.Parser;

import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {
  @Ignore
  @Test
  public void loadJSONWithSingleControl() {
    String json = 
      "{" +
        "controls: {" +
          "login: {" +
            "type: window," +
            "text: ~menu.login.title," +
            "icon: \"#gui.icons.key\"," +
            "bounds.x: 490," +
            "bounds.y: 260," +
            "bounds.w: 300," +
            "bounds.h: 200" +
          "}" +
        "}" +
      "}";
    
    Manager.registerContext(malachite.gfx.gl21.Context.class);
    
    Context _context = Manager.create(ctx -> {
      ctx.setResizable(true);
      ctx.setWH(1280, 720);
      ctx.setFPSTarget(60);
      ctx.setContextListener(new ContextListenerAdapter() {
        @Override public void onRun() {
          Parser parser = new Parser();
          GUI gui = parser.load(new JSONObject(json));
          gui.push();
        }
      });
    });
    
    _context.run();
  }
  
  @Test
  public void loadJSONWithLateAssignment() {
    String json = 
        "{" +
          "controls: {" +
            "login: {" +
              "type: window," +
              "text: ~menu.login.title," +
              "icon: \"#gui.icons.key\"," +
              "bounds.x: 490," +
              "bounds.y: 260," +
              "bounds.w: 300," +
              "bounds.h: 200," +
              
              "controls: {" +
                "textbox: {" +
                  "type: textbox," +
                  "placeholder: Test," +
                  "bounds.wh.bind_x: @login.content_bounds.wh," +
                  "bounds.x: 4," +
                  "bounds.y: 4," +
                  "bounds.w: -8," +
                  "bounds.h: 20" +
                "}" +
              "}" +
            "}" +
          "}" +
        "}";
      
      Manager.registerContext(malachite.gfx.gl21.Context.class);
      
      Context _context = Manager.create(ctx -> {
        ctx.setResizable(true);
        ctx.setWH(1280, 720);
        ctx.setFPSTarget(60);
        ctx.setContextListener(new ContextListenerAdapter() {
          @Override public void onRun() {
            Parser parser = new Parser();
            GUI gui = parser.load(new JSONObject(json));
            gui.push();
          }
        });
      });
      
      _context.run();
  }
}