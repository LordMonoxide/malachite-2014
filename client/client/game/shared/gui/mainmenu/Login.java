package shared.gui.mainmenu;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lwjgl.input.Keyboard;

import api.Lang;
import api.Lang.MenuKeys;
import api.models.User;
import gfx.fonts.TextStream;
import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.builtin.Tooltip;
import gfx.gui.control.Button;
import gfx.gui.control.Textbox;
import gfx.gui.control.Window;
import gfx.textures.TextureBuilder;

public class Login extends Window<Login.Events> {
  private Textbox _txtEmail;
  private Textbox _txtPassword;
  private Button  _btnLogin;
  
  public Login() {
    super();
    
    _events = new Events(this);
    
    ControlEvents.Key loginSubmit = new ControlEvents.Key() {
      @Override public void text(char key) { }
      @Override public void down(int key, boolean repeat) { }
      @Override public void up(int key) {
        if(key == Keyboard.KEY_RETURN) {
          login();
        }
      }
    };
    
    _txtEmail = new Textbox();
    _txtEmail.setXY(4, 4);
    _txtEmail.setH(20);
    _txtEmail.setTextPlaceholder(Lang.Menu.get(MenuKeys.LOGIN_EMAIL));
    _txtEmail.events().addKeyHandler(loginSubmit);
    
    _txtPassword = new Textbox();
    _txtPassword.setXY(_txtEmail.getX(), _txtEmail.getY() + _txtEmail.getH() + 8);
    _txtPassword.setH(_txtEmail.getH());
    _txtPassword.mask();
    _txtPassword.setTextPlaceholder(Lang.Menu.get(MenuKeys.LOGIN_PASSWORD));
    _txtPassword.events().addKeyHandler(loginSubmit);
    
    _btnLogin = new Button();
    _btnLogin.setY(_txtPassword.getY() + _txtPassword.getH() + 8);
    _btnLogin.setAutoSize(true);
    _btnLogin.setText(Lang.Menu.get(MenuKeys.LOGIN_LOGIN));
    _btnLogin.events().addClickHandler(new ControlEvents.Click() {
      @Override public void clickDbl() { }
      @Override public void click() {
        login();
      }
    });
    
    controls().add(_txtEmail);
    controls().add(_txtPassword);
    controls().add(_btnLogin);
    
    setWH(300, 200);
    
    setText(Lang.Menu.get(MenuKeys.LOGIN_TITLE));
    setIcon(TextureBuilder.getInstance().getTexture("gui/icons/key.png"));
  }
  
  @Override protected void resize() {
    super.resize();
    
    _txtEmail.setW(getContentW() - _txtEmail.getX() * 2);
    _txtPassword.setW(_txtEmail.getW());
    _btnLogin.setX(_txtPassword.getW() - _btnLogin.getW());
  }
  
  @Override public void show() {
    super.show();
    _txtEmail.setFocus(true);
  }
  
  public String getEmail   () { return _txtEmail   .getText(); }
  public String getPassword() { return _txtPassword.getText(); }
  
  public void setEmail   (String email   ) { _txtEmail   .setText(email   ); }
  public void setPassword(String password) { _txtPassword.setText(password); }
  
  private void login() {
    events().raiseLogin();
  }
  
  protected void showErrors(JSONObject errors) {
    System.err.println(errors);
    
    showValidationError(errors, User.KEY_PASSWORD, _txtPassword);
    showValidationError(errors, User.KEY_EMAIL,    _txtEmail);
  }
  
  private void showTooltip(Control<? extends ControlEvents> anchor, TextStream text) {
    new Tooltip(anchor, text).push();
  }
  
  private void showValidationError(JSONObject errors, String key, Control<? extends ControlEvents> anchor) {
    if(errors.has(key)) {
      TextStream ts = new TextStream();
      JSONArray e = errors.getJSONArray(key);
      for(int i = 0; i < e.length(); i++) {
        ts.insert(e.getString(i));
      }
      
      showTooltip(anchor, ts);
      anchor.setFocus(true);
    }
  }
  
  public static class Events extends Window.Events {
    private Deque<Event> _login = new ConcurrentLinkedDeque<>();
    
    public void addLoginHandler(Event e) { _login.add(e); }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void raiseLogin() {
      for(Event e : _login) {
        e.run();
      }
    }
    
    public interface Event {
      public abstract void run();
    }
  }
}