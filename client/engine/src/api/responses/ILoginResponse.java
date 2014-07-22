package api.responses;

import org.json.JSONObject;

public interface ILoginResponse extends IGenericResponse {
  public void success();
  public void invalid(JSONObject errors);
}