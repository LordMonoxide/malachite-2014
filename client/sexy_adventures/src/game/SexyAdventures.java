package game;

import malachite.game.Game;

public class SexyAdventures {
  public static void main(String[] args) {
    new SexyAdventures();
  }
  
  public SexyAdventures() {
    Game game = new Game();
    game.init("main_menu", new MainMenuEvents());
    game.run();
  }
}