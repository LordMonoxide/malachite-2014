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
import malachite.gfx.fonts.FontBuilder;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.ControlEvents;
import malachite.gfx.gui.builtin.Tooltip;
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
  
  private Window<?> wndLogin, wndRegister, wndChars, wndNewChar;
  private Textbox loginEmail;
  private Textbox loginPassword;
  private Button  loginSubmit;
  private Button  loginRegister;
  
  private Textbox registerEmail;
  private Textbox registerPassword;
  private Textbox registerPassword2;
  private Button  registerSubmit;
  
  private List<Character> charsList;
  private Button          charUse;
  private Button          charNew;
  private Button          charDel;
  
  private Textbox newCharName;
  private Button  newCharCreate;
  
  public MainMenuEvents(AccountGatewayInterface gateway) {
    _gateway = Objects.requireNonNull(gateway, "Account gateway must not be null");
    
    try {
      _conf = new Conf("../account.conf");
    } catch(JSONException | IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    wndLogin    = (Window<?>)controls.get("login");
    wndRegister = (Window<?>)controls.get("register");
    wndChars    = (Window<?>)controls.get("chars");
    wndNewChar  = (Window<?>)controls.get("new_char");
    
    loginEmail    = (Textbox)controls.get("login_email");
    loginPassword = (Textbox)controls.get("login_password");
    loginSubmit   = (Button) controls.get("login_submit");
    loginRegister = (Button) controls.get("show_register");
    
    registerEmail     = (Textbox)controls.get("register_email");
    registerPassword  = (Textbox)controls.get("register_password");
    registerPassword2 = (Textbox)controls.get("register_password_confirm");
    registerSubmit    = (Button) controls.get("register_submit");
    
    charsList = (List<Character>)controls.get("chars_list");
    charUse   = (Button)         controls.get("char_use");
    charNew   = (Button)         controls.get("char_new");
    charDel   = (Button)         controls.get("char_del");
    
    newCharName   = (Textbox)controls.get("new_char_name");
    newCharCreate = (Button)controls.get("new_char_create");
    
    registerEvents();
    
    loginEmail.setText((String)_conf.get("account.email"));
    
    if(loginEmail.hasText()) {
      loginPassword.setFocus(true);
    } else {
      loginEmail.setFocus(true);
    }
  }
  
  @Override public void postLayout() {
    //TODO: Not this hacky thing
    FontBuilder.getInstance().getDefault().events().addLoadHandler(() -> {
      loginSubmit  .bounds.setX(-loginSubmit  .bounds.getW());
      loginRegister.bounds.setX(-loginRegister.bounds.getW() - 4);

      charUse.bounds.setX(-charUse.bounds.getW() - 4);
      charUse.bounds.setY(-charUse.bounds.getH() - 4);
      charNew.bounds.setX(-charNew.bounds.getW() - 4);
      charDel.bounds.setX(-charDel.bounds.getW() - 4);
      
      newCharCreate.bounds.setX(-newCharCreate.bounds.getW() - 4);
      newCharCreate.bounds.setY(-newCharCreate.bounds.getH() - 4);
    });
  }
  
  @Override public void resize() {
    
  }
  
  @Override public void draw() {
    
  }
  
  @Override public void destroy() {
    
  }
  
  private void registerEvents() {
    ControlEvents.KeyEvent login = ev -> {
      if(ev.key == 28) {
        
      }
    };
    
    ControlEvents.KeyEvent register = ev -> {
      if(ev.key == 28) {
        
      }
    };
    
    loginEmail.events().onKeyDown(login);
    loginPassword.events().onKeyDown(login);
    
    registerEmail.events().onKeyDown(register);
    registerPassword.events().onKeyDown(register);
    registerPassword2.events().onKeyDown(register);
  }
  
  public void showRegisterClick(ControlEvents.ClickEventData e) { showRegister(); }
  public void newCharClick     (ControlEvents.ClickEventData e) { showNewChar (); }
  
  public void login(String email, String password) throws Exception {
    try {
      _conf.set("account.email", email);
      _conf.save();
      
      _user = _gateway.login(email, password);
      wndLogin.hide();
      showChars();
    } catch(AccountException.InvalidLoginCredentials e) {
      showValidationError("Invalid email or password", loginEmail);
    } catch(ValidatorException.ValidationFailure e) {
      Control<?> c = null;
      
      switch(e.name) {
        case "email":    c = loginEmail;    break;
        case "password": c = loginPassword; break;
      }
      
      showValidationError(e.message, c);
    }
  }
  
  public void register(String email, String password, String passwordConfirm) throws Exception {
    if(!password.equals(passwordConfirm)) {
      showValidationError("Passwords don't match", registerPassword2);
    }
    
    try {
      _user = _gateway.register(email, password);
      wndRegister.hide();
      showChars();
    } catch(ValidatorException.ValidationFailure e) {
      Control<?> c = null;
      
      switch(e.name) {
        case "email":    c = registerEmail;    break;
        case "password": c = registerPassword; break;
      }
      
      showValidationError(e.message, c);
    }
  }
  
  public void showRegister() {
    wndLogin.hide();
    wndRegister.show();
  }
  
  public void showChars() throws AccountException, Exception {
    charsList.clear();
    
    for(Character character : _user.characters) {
      charsList.add(character.name, character);
    }
    
    wndChars.show();
  }
  
  public void showNewChar() {
    wndChars.hide();
    wndNewChar.show();
  }
  
  public void createChar(String name) throws AccountException, Exception {
    _user.characters.add(name);
    wndNewChar.hide();
    showChars();
  }
  
  private void showValidationError(String error, Control<? extends ControlEvents> anchor) {
    showTooltip(anchor, error);
    anchor.setFocus(true);
  }
  
  private void showTooltip(Control<? extends ControlEvents> anchor, String text) {
    new Tooltip(anchor, text).push();
  }
}