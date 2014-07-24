package gfx.util;

public class StringUtils {
  public static String snakeToProper(String snake) {
    String[] parts = snake.split("_");
    String camel = "";
    
    for(String part : parts) {
      camel += capitolizeFirst(part);
    }
    
    return camel;
  }
  
  public static String snakeToCamel(String snake) {
    String[] parts = snake.split("_");
    String camel = parts[0];
    
    for(int i = 1; i < parts.length; i++) {
      camel += capitolizeFirst(parts[i]);
    }
    
    return camel;
  }
  
  public static String capitolizeFirst(String s) {
    return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
  }
}