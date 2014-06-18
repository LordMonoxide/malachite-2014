package malachite.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import malachite.engine.data.MapInterface;

public final class Engine {
  public static void main(String... args) {
    try(Scanner in = new Scanner(System.in)) {
      GameInterface game = null;
      
      for(;;) {
        System.out.print("[Server]/Client? "); //$NON-NLS-1$
        String input = in.nextLine().toLowerCase();
        
        if("server".startsWith(input)) { //$NON-NLS-1$
          game = new malachite.server.Game();
          break;
        }
        
        if("client".startsWith(input)) { //$NON-NLS-1$
          game = new malachite.client.Game();
          break;
        }
      }
      
      game.start();
    }
  }
  
  private Engine() { }
  
  private static Class<? extends MapInterface> _map;
  
  public static void create(Class<? extends MapInterface> map) {
    _map = map;
  }
  
  public static MapInterface newMap(int x, int y) {
    try {
      return _map.getConstructor(int.class, int.class).newInstance(x, y);
    } catch(InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e1) {
      e1.printStackTrace();
    }
    
    
    return null;
  }
}