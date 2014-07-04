package shared;

import shared.gui.mainmenu.IMainMenuProvider;
import shared.gui.mainmenu.MainMenuProvider;
import api.gateways.HTTPAuthGateway;
import api.gateways.IAuthGateway;
import api.repositories.HTTPUserRepository;
import api.repositories.IUserRepository;

public final class Instances {
  public static IUserRepository newUserRepository() {
    return new HTTPUserRepository();
  }
  
  public static IAuthGateway newAuthGateway() {
    return new HTTPAuthGateway();
  }
  
  public static IMainMenuProvider newMainMenuProvider() {
    return new MainMenuProvider(newAuthGateway());
  }
  
  public static IMainMenuProvider newMainMenuProvider(IAuthGateway auth) {
    return new MainMenuProvider(auth);
  }
  
  private Instances() { }
}