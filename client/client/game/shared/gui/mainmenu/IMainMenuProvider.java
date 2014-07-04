package shared.gui.mainmenu;

public interface IMainMenuProvider {
  public void setMainMenu(IMainMenu menu);
  public void login(String email, String password);
}