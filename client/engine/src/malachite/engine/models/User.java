package malachite.engine.models;

public class User {
  public static final String DB_EMAIL    = "email";    //$NON-NLS-1$
  public static final String DB_PASSWORD = "password"; //$NON-NLS-1$
  
  public final String email;
  
  public User(String email) {
    this.email = email;
  }
}