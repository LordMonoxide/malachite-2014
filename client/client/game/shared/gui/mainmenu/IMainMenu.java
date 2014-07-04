package shared.gui.mainmenu;

import org.json.JSONException;
import org.json.JSONObject;

public interface IMainMenu {
  public void showLogin();
  public void loggingIn();
  public void loginSuccess();
  public void loginError(JSONObject errors);
  
  public void showError    (String source);
  public void showJSONError(String source, JSONException e);
}