package api;


public interface IGenericResponse extends IErrorResponse {
  public abstract void loginRequired();
}