package shared;

import org.json.JSONException;
import org.json.JSONObject;

import api.IErrorResponse;
import api.IGenericResponse;
import api.Instances;
import api.Lang;
import api.gateways.IAuthGateway;
import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;
import gfx.gui.GUI;
import net.http.Request;
import shared.gui.mainmenu.MainMenu;

public class Game {
  private Context _context;
  
  private IMainMenu _menu;
  
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
          _menu = new MainMenu(new MainMenuProvider(Instances.newAuthGateway()));
          _menu.showLogin();
          ((GUI)_menu).push();
        }
        
        @Override public void onClosed() {
          Request.destroy();
        }
      });
    });
    
    _context.run();
  }
  
  public class MainMenuProvider implements IMainMenuProvider {
    private IAuthGateway _auth;
    
    public MainMenuProvider(IAuthGateway auth) {
      _auth = auth;
    }
    
    @Override public void login(String email, String password) {
      class R extends GenericResponse implements IAuthGateway.LoginResponse {
        @Override public void success() {
          _menu.loginSuccess();
        }
        
        @Override public void invalid(JSONObject errors) {
          _menu.loginError(errors);
        }
      }
      
      _menu.loggingIn();
      _auth.login(email, password, new R());
    }
    
    private class ErrorResponse implements IErrorResponse {
      @Override public void error(String source) {
        _menu.showError(source);
      }
      
      @Override public void jsonError(String source, JSONException e) {
        _menu.showJSONError(source, e);
      }
    }
    
    private class GenericResponse extends ErrorResponse implements IGenericResponse {
      @Override //TODO
      public void loginRequired() {
        _menu.showLogin();
      }
    }
  }
}