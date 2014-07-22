package game;

import gfx.gui.Control;
import gfx.gui.ControlEvents;
import gfx.gui.control.Button;
import gfx.gui.control.Textbox;
import gfx.gui.control.Window;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public abstract class BasicLoginWindow extends Window<BasicLoginWindow.Events> {
  protected Textbox _txtEmail;
  protected Textbox _txtPassword;
  protected Button  _btnLogin;
  
  public BasicLoginWindow() {
    _events = new Events(this);
  }
  
  public abstract String getEmail();
  public abstract String getPassword();
  
  public abstract void setEmail   (String email   );
  public abstract void setPassword(String password);
  
  protected void login() {
    events().onLogin(getEmail(), getPassword());
  }
  
  public static class Events extends Window.Events {
    private Deque<LoginEvent> _login = new ConcurrentLinkedDeque<>();
    
    public Events onLogin(LoginEvent e) { _login.add(e); return this; }
    
    protected Events(Control<? extends ControlEvents> c) {
      super(c);
    }
    
    public void onLogin(String email, String password) {
      for(LoginEvent e : _login) {
        e.event(new LoginEventData(_control, email, password));
      }
    }
    
    public interface LoginEvent extends Event<LoginEventData> { }
    
    public class LoginEventData extends ControlEvents.EventData {
      public final String email, password;
      
      protected LoginEventData(Control<? extends ControlEvents> control, String email, String password) {
        super(control);
        this.email = email;
        this.password = password;
      }
    }
  }
}