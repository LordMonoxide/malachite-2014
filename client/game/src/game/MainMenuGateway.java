package game;

import org.json.JSONException;
import org.json.JSONObject;

import api.gateways.IUserGateway;

public abstract class MainMenuGateway {
  private IUserGateway _gateway;
  
  protected MainMenuGateway(IUserGateway gateway) {
    _gateway = gateway;
  }
  
  public abstract void loggingIn();
  public abstract void loginSuccess();
  public abstract void loginError(JSONObject errors);
  
  public abstract void showError    (String source);
  public abstract void showJSONError(String source, JSONException e);
}