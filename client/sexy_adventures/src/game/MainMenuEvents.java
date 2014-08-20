package game;

import java.util.Map;
import java.util.Objects;

import malachite.engine.gateways.AccountGatewayInterface;
import malachite.gfx.gui.Control;
import malachite.gfx.gui.control.Button;
import malachite.gfx.gui.control.Textbox;
import malachite.gfx.gui.parser.GUIEvents;

public class MainMenuEvents implements GUIEvents {
  private AccountGatewayInterface _gateway;
  
  private Textbox txtEmail;
  private Textbox txtPassword;
  private Button  btnSubmit;
  
  public MainMenuEvents(AccountGatewayInterface gateway) {
    _gateway = Objects.requireNonNull(gateway, "Account gateway must not be null");
  }
  
  @Override public void registerControls(Map<String, Control<?>> controls) {
    txtEmail    = (Textbox)controls.get("login_email");
    txtPassword = (Textbox)controls.get("login_password");
    btnSubmit   = (Button) controls.get("login_submit");
  }
  
  public void login(String email, String password) throws Exception {
    _gateway.login(email, password);
  }
}