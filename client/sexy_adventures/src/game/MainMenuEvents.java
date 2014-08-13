package game;

import java.util.Map;

import malachite.gfx.gui.Control;
import malachite.gfx.gui.control.Button;
import malachite.gfx.gui.control.Textbox;
import malachite.gfx.gui.parser.GUIEvents;

public class MainMenuEvents implements GUIEvents {
  private Textbox txtEmail;
  private Textbox txtPassword;
  private Button  btnSubmit;
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    txtEmail    = (Textbox)controls.get("login_email");
    txtPassword = (Textbox)controls.get("login_password");
    btnSubmit   = (Button) controls.get("login_submit");
  }
  
  public void login(String email, String password) {
    
  }
}