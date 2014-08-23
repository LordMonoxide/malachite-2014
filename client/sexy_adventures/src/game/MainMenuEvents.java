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
import malachite.validator.Validator.ValidatorException;

public class MainMenuEvents implements GUIEvents {
  private AccountGatewayInterface _gateway;
  
  private Window<?> login, register;
  private Textbox loginEmail;
  private Textbox loginPassword;
  private Button  loginSubmit;
  
  private Textbox registerEmail;
  private Textbox registerPassword;
  private Textbox registerPassword2;
  private Button  registerSubmit;
  
  public MainMenuEvents(AccountGatewayInterface gateway) {
    _gateway = Objects.requireNonNull(gateway, "Account gateway must not be null");
  }
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    login    = (Window<?>)controls.get("login");
    register = (Window<?>)controls.get("register");
    
    loginEmail    = (Textbox)controls.get("login_email");
    loginPassword = (Textbox)controls.get("login_password");
    loginSubmit   = (Button) controls.get("login_submit");
    
    registerEmail     = (Textbox)controls.get("register_email");
    registerPassword  = (Textbox)controls.get("register_password");
    registerPassword2 = (Textbox)controls.get("register_password_confirm");
    registerSubmit    = (Button) controls.get("register_submit");
    
    loginEmail.setFocus(true);
  }
  
  public void showRegisterClick(ControlEvents.ClickEventData e) {
    showRegister();
  }
  
  public void login(String email, String password) throws Exception {
    try {
      _gateway.login(email, password);
    } catch(AccountException.InvalidLoginCredentials e) {
      System.err.println("Invalid email/password");
    } catch(ValidatorException e) {
      System.err.println(e.getMessage());
    }
  }
  
  public void register(String email, String password, String passwordConfirm) throws Exception {
    if(!password.equals(passwordConfirm)) {
      System.err.println("Passwords don't match");
    }
    
    try {
      _gateway.register(email, password);
    } catch(ValidatorException e) {
      System.err.println(e.getMessage());
    }
  }
  
  public void showRegister() {
    login.hide();
    register.show();
  }
}