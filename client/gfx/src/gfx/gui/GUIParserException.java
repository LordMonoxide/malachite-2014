package gfx.gui;

public class GUIParserException extends Exception {
  private static final long serialVersionUID = 2679533898107550451L;
  GUIParserException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public static class EngineException extends GUIParserException {
    private static final long serialVersionUID = 876313448633091747L;
    EngineException(Throwable cause) {
      super("Something went wrong. This is most likely a bug. See cause below for details.", cause);
    }
  }
  
  public static class NoSuchControlException extends GUIParserException {
    private static final long serialVersionUID = 7063018773137033515L;
    NoSuchControlException(String name, Throwable cause) {
      super("There is no control by the name of \"" + name + "\"", cause);
    }
  }
  
  public static class NoSuchMemberException extends GUIParserException {
    private static final long serialVersionUID = 7063018773137033515L;
    NoSuchMemberException(Object object, String member, Throwable cause) {
      super("There is member of \"" + object + "\" by the name of \"" + member + "\"", cause);
    }
  }
  
  public static class SyntaxException extends GUIParserException {
    private static final long serialVersionUID = 7063018773137033515L;
    SyntaxException(Throwable cause) {
      this("There is an error in the syntax of your menu.", cause);
    }
    
    SyntaxException(String message, Throwable cause) {
      super(message, cause);
    }
  }
}