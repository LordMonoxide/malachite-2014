package game;

import java.util.Map;
import java.util.Objects;

import malachite.engine.exceptions.AccountException;
import malachite.engine.gateways.AccountGatewayInterface;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.control.Button;
import malachite.gfx.gui.control.Textbox;
import malachite.gfx.gui.control.Window;
import malachite.gfx.gui.parser.GUIEvents;

public class MainMenuEvents implements GUIEvents {
  private AccountGatewayInterface _gateway;
  
  private Window<?> login, register;
  private Textbox txtEmail;
  private Textbox txtPassword;
  private Button  btnSubmit;
  
  public MainMenuEvents(AccountGatewayInterface gateway) {
    _gateway = Objects.requireNonNull(gateway, "Account gateway must not be null");
  }
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    login    = (Window<?>)controls.get("login");
    register = (Window<?>)controls.get("register");
    
    txtEmail    = (Textbox)controls.get("login_email");
    txtPassword = (Textbox)controls.get("login_password");
    btnSubmit   = (Button) controls.get("login_submit");
  }
  
  public void showRegisterClick(ControlEvents.ClickEventData e) {
    showRegister();
  }
  
  public void login(String email, String password) throws Exception {
    try {
      _gateway.login(email, password);
    } catch(AccountException.InvalidLoginCredentials e) {
      System.err.println("Invalid email/password");
    }
  }
  
  public void register(String email, String password, String passwordConfirm) throws Exception {
    
  }
  
  public void showRegister() {
    login.hide();
    register.show();
  }
}