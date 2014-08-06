package malachite.gfx.gui.parser;

public class ParserException extends Exception {
  private static final long serialVersionUID = 1L;
  ParserException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public static class EngineException extends ParserException {
    private static final long serialVersionUID = 1L;
    EngineException(Throwable cause) {
      super("Something went wrong. This is most likely a bug. See cause below for details.", cause);
    }
  }
  
  public static class NoSuchControlException extends ParserException {
    private static final long serialVersionUID = 1L;
    NoSuchControlException(String name, Throwable cause) {
      super("There is no control by the name of \"" + name + "\"", cause);
    }
  }
  
  public static class NoSuchMemberException extends ParserException {
    private static final long serialVersionUID = 1L;
    NoSuchMemberException(Object object, String member, Throwable cause) {
      super("There is member of \"" + object + "\" by the name of \"" + member + "\"", cause);
    }
  }
  
  public static class SyntaxException extends ParserException {
    private static final long serialVersionUID = 1L;
    SyntaxException(Throwable cause) {
      this("There is an error in the syntax of your menu.", cause);
    }
    
    SyntaxException(String message, Throwable cause) {
      super(message, cause);
    }
  }
  
  public static class InvalidCallbackException extends ParserException {
    private static final long serialVersionUID = 1L;
    InvalidCallbackException(String callback, String control, Throwable cause) {
      super("The callback \"" + callback + "\" does not exist for control \"" + control + "\".", cause);
    }
  }
  
  public static class NoEventListenerException extends ParserException {
    private static final long serialVersionUID = 1L;
    NoEventListenerException(String event, String control, Throwable cause) {
      super("The event \"" + event + "\" for control \"" + control + "\" is not being listened for.", cause);
    }
  }
}