package malachite.validator;

import java.util.HashMap;
import java.util.Map;

public class Validator {
  private static Map<String, Rule> _rules = new HashMap<>();
  
  public static void addRule(String name, Rule rule) {
    _rules.put(name, rule);
  }
  
  static {
    addRule("email",    new Email()); //$NON-NLS-1$
    addRule("password", new Password()); //$NON-NLS-1$
  }
  
  public Validator check(Object value, String... rules) throws ValidatorException {
    for(String rule : rules) {
      Rule r = _rules.get(rule);
      
      if(r == null) {
        throw new ValidatorException.NoSuchRule(rule);
      }
      
      if(!r.check(value)) {
        throw new ValidatorException.ValidationFailure(r, value);
      }
    }
    
    return this;
  }
  
  public interface Rule {
    public boolean check(Object value) throws ValidatorException;
  }
  
  public static class Email implements Rule {
    @Override public boolean check(Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype(this, value); //$NON-NLS-1$
      }
      
      String s = (String)value;
      if(!s.contains("@")) { return false; }
      if(s.length() <   3) { return false; }
      if(s.length() > 254) { return false; }
      
      return true;
    }
  }
  
  public static class Password implements Rule {
    @Override public boolean check(Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype(this, value); //$NON-NLS-1$
      }
      
      String s = (String)value;
      if(s.length() < 6) { return false; }
      
      return true;
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
    
    public static class NoSuchRule extends ValidatorException {
      private static final long serialVersionUID = 1L;
      
      public NoSuchRule(String rule) {
        super("There is no such rule by the name of " + rule); //$NON-NLS-1$
      }
    }
    
    public static class ValidationFailure extends ValidatorException {
      private static final long serialVersionUID = 1L;
      
      public ValidationFailure(Rule rule, Object value) {
        super("The validation of " + value + " failed on rule " + rule.getClass().getSimpleName()); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
    
    public static class UnsupportedDatatype extends ValidatorException {
      private static final long serialVersionUID = 1L;
      
      public UnsupportedDatatype(Rule rule, Object value) {
        super("Unsupported datatype in " + rule.getClass().getName() + ": " + value.getClass().getSimpleName()); //$NON-NLS-1$ //$NON-NLS-2$
      }
    }
  }
}