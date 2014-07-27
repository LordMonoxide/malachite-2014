package shared;

import org.json.JSONException;
import org.json.JSONObject;

import api.Instances;
import api.Lang;
import api.gateways.IAuthGateway;
import api.responses.IErrorResponse;
import api.responses.IGenericResponse;
import api.responses.ILoginResponse;
import game.MainMenuListenerInterface;
import gfx.Context;
import gfx.ContextListenerAdapter;
import gfx.Manager;
import gfx.gui.GUI;
import shared.gui.mainmenu.MainMenu;

public class Game {
  private Context _context;
  
  private MainMenuListenerInterface _menu;
  
  public static void main(String[] args) {
    Game game = new Game();
    game.start();
  }
  
  public void start() {
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
      class R extends GenericResponse implements ILoginResponse {
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