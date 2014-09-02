package game;

import java.io.IOException;

import malachite.game.Game;
import malachite.gfx.gui.GUI;
import malachite.gfx.gui.GUIManager;
import malachite.overloader.Loader;

public class SexyAdventures extends Game {
  public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
    Loader loader = new Loader(Loader.class.getClassLoader());
    
    loader.bind("malachite.engine.providers.GatewayProvider", "malachite.engine.providers.JDBCGatewayProvider");
    loader.bind("malachite.engine.jdbc.JDBCInitializer", "malachite.engine.jdbc.MySQLInitializer");
    
    loader.bind("malachite.engine.providers.SecurityProvider", "malachite.engine.providers.DefaultSecurityProvider");
    
    loader.bind("malachite.engine.providers.LangProvider", "malachite.engine.providers.ConfLangProvider");
    
    loader.create("game.SexyAdventures");
  }
  
  public SexyAdventures() throws Exception {
    run();
  }
  
  @Override protected GUI createInitialGUI(GUIManager guis) throws IOException {
    return guis.loadFromFile("main_menu", new MainMenuEvents(engine.providers.gateways.accountGateway()));
  }
}