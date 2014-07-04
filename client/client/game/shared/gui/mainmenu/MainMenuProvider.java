package shared.gui.mainmenu;

import org.json.JSONException;
import org.json.JSONObject;

import api.IErrorResponse;
import api.IGenericResponse;
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