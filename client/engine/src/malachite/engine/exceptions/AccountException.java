package malachite.engine.exceptions;

public abstract class AccountException extends Exception {
  private static final long serialVersionUID = 1L;

  public static class InvalidLoginCredentials extends AccountException {
    private static final long serialVersionUID = 1L;
  }
  
  public static class AccountAlreadyExists extends AccountException {
    private static final long serialVersionUID = 1L;
  }
}