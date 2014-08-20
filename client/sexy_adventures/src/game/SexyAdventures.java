package game;

import java.io.IOException;
import java.sql.SQLException;

import malachite.engine.EngineBuilder;
import malachite.engine.providers.JDBCGatewayProvider;
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
    JDBCGatewayProvider.JDBCInitializer initializer = new JDBCGatewayProvider.MySQLInitializer("localhost", "malachite", "malachite", "malachite");
    
    try {
      engine.withGatewayProvider(new JDBCGatewayProvider(initializer));
    } catch(SQLException e) {
      throw new RuntimeException(e);
    }
    
    System.out.println(engine);
  }
}