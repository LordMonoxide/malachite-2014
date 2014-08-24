package malachite.engine.models;

public abstract class User<T> {
  public static final String TABLE       = "users";    //$NON-NLS-1$
  public static final String DB_ID       = "id";       //$NON-NLS-1$
  public static final String DB_EMAIL    = "email";    //$NON-NLS-1$
  public static final String DB_PASSWORD = "password"; //$NON-NLS-1$
  
  private T id;
  
  public final String email;
  
  public User(T id, String email) {
    this.id    = id;
    this.email = email;
  }
}