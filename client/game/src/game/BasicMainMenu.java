package game;

import gfx.gui.GUI;

import org.json.JSONException;
import org.json.JSONObject;

import api.gateways.IUserGateway;

public abstract class BasicMainMenu extends GUI {
  private IUserGateway _gateway;
  
  protected BasicMainMenu(IUserGateway gateway) {
    _gateway = gateway;
    ready();
  }
  
  public abstract void loggingIn();
  public abstract void loginSuccess();
  public abstract void loginError(JSONObject errors);
  
  public abstract void showError    (String source);
  public abstract void showJSONError(String source, JSONException e);
}