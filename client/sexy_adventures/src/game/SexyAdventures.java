package game;

import java.io.IOException;

import malachite.engine.EngineBuilder;
import malachite.game.Game;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.GUIManager;

public class SexyAdventures extends Game {
  public static void main(String[] args) {
    new SexyAdventures();
  }
  
  public SexyAdventures() {
    run();
  }
  
  @Override protected GUI createInitialGUI(GUIManager guis) throws IOException {
    return guis.loadFromFile("main_menu", new MainMenuEvents(engine.accountGateway));
  }
  
  @Override protected void setupEngine(EngineBuilder engine) {
    System.out.println(engine);
  }
}