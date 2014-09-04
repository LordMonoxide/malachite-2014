package malachite.validator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Validator {
  private static Map<String, Rule> _rules = new HashMap<>();
  
  public static void addRule(String name, Rule rule) {
    _rules.put(name, rule);
  }
  
  static {
    addRule("name",     new Name());     //$NON-NLS-1$
    addRule("email",    new Email());    //$NON-NLS-1$
    addRule("password", new Password()); //$NON-NLS-1$
  }
  
  public Validator check(Object value, String name, String... rules) throws ValidatorException {
    for(String rule : rules) {
      Rule r = _rules.get(rule);
      
      if(r == null) {
        throw new ValidatorException.NoSuchRule(rule);
      }
      
      r.check(name, value);
    }
    
    return this;
  }
  
  public interface Rule {
    public void check(String name, Object value) throws ValidatorException;
  }
  
  public static class Name implements Rule {
    private Pattern _pattern = Pattern.compile("^[\\p{L}\\d`~!@#$%^&*()\\-_=+,\\.<>\\/?;:'\"\\[\\]{}\\\\| ]+$");
    
    @Override public void check(String name, Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype(this, value); //$NON-NLS-1$
      }
      
      String s = (String)value;
      if(s.length() <  4)                { throw new ValidatorException.ValidationFailure(this, name, "Names must be at least 4 characters long"); }
      if(s.length() > 20)                { throw new ValidatorException.ValidationFailure(this, name, "Names must be less than 20 characters long"); }
      if(!_pattern.matcher(s).matches()) { throw new ValidatorException.ValidationFailure(this, name, "You have invalid characters in your name (how did you even do that?)"); }
    }
  }
  
  public static class Email implements Rule {
    @Override public void check(String name, Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype(this, value); //$NON-NLS-1$
      }
      
      String s = (String)value;
      if(s.length() <   3) { throw new ValidatorException.ValidationFailure(this, name, "Emails must be at least 3 characters long"); }
      if(s.length() > 254) { throw new ValidatorException.ValidationFailure(this, name, "Emails must be less than 254 characters long"); }
      if(!s.contains("@")) { throw new ValidatorException.ValidationFailure(this, name, "Please enter a valid email address"); }
    }
  }
  
  public static class Password implements Rule {
    @Override public void check(String name, Object value) throws ValidatorException {
      if(!(value instanceof String)) {
        throw new ValidatorException.UnsupportedDatatype(this, value); //$NON-NLS-1$
      }
      
      String s = (String)value;
      if(s.length() < 6) { throw new ValidatorException.ValidationFailure(this, name, "Passwords must be at least 6 characters long"); }
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
      
      public final Rule   rule;
      public final String name;
      public final String message;
      
      public ValidationFailure(Rule rule, String name, String message) {
        super("The validation of \"" + name + "\" failed on rule " + rule.getClass().getSimpleName() + " (" + message + ')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        this.rule    = rule;
        this.name    = name;
        this.message = message;
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