package malachite.gfx.gui.parser;

import malachite.gfx.util.BoundMember;

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
      super("There is no member of \"" + object + "\" by the name of \"" + member + "\"", cause);
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
  
  public static class LateAssignmentException extends ParserException {
    private static final long serialVersionUID = 1L;
    LateAssignmentException(String message, Throwable cause) {
      super(message, cause);
    }
  }
  
  public static class ErrorGettingValueException extends LateAssignmentException {
    private static final long serialVersionUID = 1L;
    ErrorGettingValueException(BoundMember bm, Throwable cause) {
      super("An error occurred while getting the value for a late assignment.", cause);
    }
  }
  
  public static class ErrorSettingValueException extends LateAssignmentException {
    private static final long serialVersionUID = 1L;
    ErrorSettingValueException(BoundMember bm, Throwable cause) {
      super("An error occurred while setting the value for a late assignment.", cause);
    }
  }
  
  public static class EventException extends ParserException {
    private static final long serialVersionUID = 1L;
    EventException(String message, Throwable cause) {
      super(message, cause);
    }
  }
  
  public static class ErrorParsingEventPathException extends EventException {
    private static final long serialVersionUID = 1L;
    ErrorParsingEventPathException(Throwable cause) {
      super("An error occurred while parsing an event path.", cause);
    }
  }
  
  public static class NoSuchControlInEventException extends EventException {
    private static final long serialVersionUID = 1L;
    NoSuchControlInEventException(String name, Throwable cause) {
      super("There is no control by the name of \"" + name + "\"", cause);
    }
  }
}