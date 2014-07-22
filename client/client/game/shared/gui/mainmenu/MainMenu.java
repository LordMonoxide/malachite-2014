package shared.gui.mainmenu;

import org.json.JSONException;
import org.json.JSONObject;

import shared.IMainMenuProvider;
import api.Lang;
import game.BasicMainMenu;
import gfx.gui.GUI;
import gfx.gui.builtin.Message;

public class MainMenu extends GUI implements BasicMainMenu {
  private IMainMenuProvider _provider;
  
  private Message _error;
  
  private Login _wndLogin;
  
  public MainMenu(IMainMenuProvider provider) {
    _provider = provider;
    ready();
  }
  
  @Override protected void load() {
    _wndLogin = new Login();
    _wndLogin.hide();
    _wndLogin.events().addLoginHandler(() -> {
      _provider.login(_wndLogin.getEmail(), _wndLogin.getPassword());
    });
    
    controls().add(_wndLogin);
    
    resize();
  }
  
  @Override public void destroy() {
    
  }
  
  @Override protected void resize() {
    _wndLogin.setXY((_context.getW() - _wndLogin.getW()) / 2, (_context.getH() - _wndLogin.getH()) / 2);
  }
  
  @Override protected void draw() {
    
  }
  
  @Override protected boolean logic() {
    return false;
  }
  
  @Override public void showLogin() {
    _wndLogin.show();
  }
  
  @Override public void loggingIn() {
    _wndLogin.hide();
  }
  
  @Override public void loginSuccess() {
    
  }
  
  @Override public void loginError(JSONObject errors) {
    _wndLogin.show();
    _wndLogin.showErrors(errors);
  }
  
  @Override public void showError(String source) {
    System.err.println(Lang.Menu.get(Lang.MenuKeys.ERROR_ERROR) + '\n' + source);
    _error = Message.wait(Lang.Menu.get(Lang.MenuKeys.ERROR_ERROR), source);
    _error.push();
  }
  
  @Override public void showJSONError(String source, JSONException e) {
    System.err.println(Lang.Menu.get(Lang.MenuKeys.ERROR_JSON) + '\n' + source + '\n' + e);
    _error = Message.wait(Lang.Menu.get(Lang.MenuKeys.ERROR_JSON), source + '\n' + e);
    _error.push();
  }
}