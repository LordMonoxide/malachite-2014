package malachite.validator;

import java.util.HashMap;
import java.util.Map;

public class Validator {
  private static Map<String, Rule> _rules = new HashMap<>();
  
  public static void addRule(String name, Rule rule) {
    _rules.put(name, rule);
  }
  
  static {
    addRule("email", new Email()); //$NON-NLS-1$
  }
  
  public interface Rule {
    public boolean check(Object value) throws ValidatorException;
  }
  
  public static class Email implements Rule {
    @Override public boolean check(Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype("Email", value); //$NON-NLS-1$
      }
      
      return false;
    }
  }
  
  public static class ValidatorException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public ValidatorException() {
      super();
    }
    
    public ValidatorException(String message) {
      super(message);
    }
    
    public ValidatorException(String message, Throwable cause) {
      super(message, cause);
    }
    
    public static class UnsupportedDatatype extends ValidatorException {
      private static final long serialVersionUID = 1L;
      
      public UnsupportedDatatype(String rule, Object value) {
        super("Unsupported datatype in " + rule + ": " + value.getClass().getName()); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
  }
}