package api.responses;


public interface IGenericResponse extends IErrorResponse {
  public abstract void loginRequired();
}