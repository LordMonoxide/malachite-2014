package shared.gui.mainmenu;

import org.json.JSONException;
import org.json.JSONObject;

import net.http.Response;
import api.HTTP;
import api.gateways.IAuthGateway;

public class MainMenuProvider implements IMainMenuProvider {
  private IAuthGateway _auth;
  private IMainMenu    _menu;
  
  public MainMenuProvider(IAuthGateway auth) {
    _auth = auth;
  }
  
  @Override public void setMainMenu(IMainMenu menu) {
    _menu = menu;
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
  
  private class ErrorResponse implements HTTP.ErrorResponse {
    @Override public void error(Response r) {
      _menu.showError(r);
    }
    
    @Override public void jsonError(Response r, JSONException e) {
      _menu.showJSONError(r, e);
    }
  }
  
  private class GenericResponse extends ErrorResponse implements HTTP.GenericResponse {
    @Override //TODO
    public void loginRequired() {
      _menu.showLogin();
    }
  }
}