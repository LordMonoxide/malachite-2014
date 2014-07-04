package shared.gui.mainmenu;

import net.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

public interface IMainMenu {
  public void showLogin();
  public void loggingIn();
  public void loginSuccess();
  public void loginError(JSONObject errors);
  
  public void showError    (Response r);
  public void showJSONError(Response r, JSONException e);
}