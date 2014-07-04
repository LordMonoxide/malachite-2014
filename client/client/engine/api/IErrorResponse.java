package api;

import org.json.JSONException;

public interface IErrorResponse {
  public abstract void error(String source);
  public abstract void jsonError(String source, JSONException e);
}