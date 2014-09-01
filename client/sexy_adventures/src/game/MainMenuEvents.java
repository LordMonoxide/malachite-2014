package game;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import org.json.JSONException;

import malachite.engine.conf.Conf;
import malachite.engine.exceptions.AccountException;
import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.models.Character;
import malachite.engine.models.User;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.control.Button;
import malachite.gfx.gui.control.List;
import malachite.gfx.gui.control.Textbox;
import malachite.gfx.gui.control.Window;
import malachite.gfx.gui.parser.GUIEvents;
import malachite.validator.Validator.ValidatorException;

public class MainMenuEvents implements GUIEvents {
  private AccountGatewayInterface _gateway;
  
  private Conf _conf;
  
  private User _user;
  
  private Window<?> login, register, chars;
  private Textbox loginEmail;
  private Textbox loginPassword;
  private Button  loginSubmit;
  
  private Textbox registerEmail;
  private Textbox registerPassword;
  private Textbox registerPassword2;
  private Button  registerSubmit;
  
  private List<Character> charsList;
  
  public MainMenuEvents(AccountGatewayInterface gateway) {
    _gateway = Objects.requireNonNull(gateway, "Account gateway must not be null");
    
    try {
      _conf = new Conf("../menu.conf");
    } catch(JSONException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    login    = (Window<?>)controls.get("login");
    register = (Window<?>)controls.get("register");
    chars    = (Window<?>)controls.get("chars");
    
    loginEmail    = (Textbox)controls.get("login_email");
    loginPassword = (Textbox)controls.get("login_password");
    loginSubmit   = (Button) controls.get("login_submit");
    
    registerEmail     = (Textbox)controls.get("register_email");
    registerPassword  = (Textbox)controls.get("register_password");
    registerPassword2 = (Textbox)controls.get("register_password_confirm");
    registerSubmit    = (Button) controls.get("register_submit");
    
    charsList = (List<Character>)controls.get("chars_list");
    
    loginEmail.setFocus(true);
    loginEmail.setText((String)_conf.get("account.email"));
  }
  
  public void showRegisterClick(ControlEvents.ClickEventData e) {
    showRegister();
  }
  
  public void login(String email, String password) throws Exception {
    try {
      _conf.set("account.email", email);
      _conf.save();
      
      _user = _gateway.login(email, password);
      login.hide();
      showChars();
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
      _user = _gateway.register(email, password);
      register.hide();
      showChars();
    } catch(ValidatorException e) {
      System.err.println(e.getMessage());
    }
  }
  
  public void showRegister() {
    login.hide();
    register.show();
  }
  
  public void showChars() throws AccountException, Exception {
    for(Character character : _user.characters()) {
      charsList.add(character.name, character);
    }
    
    chars.show();
  }
}