package api.gateways;

import org.json.JSONObject;

import api.Future;
import api.HTTP.GenericResponse;

public interface IAuthGateway {
  public Future login(String email, String password, LoginResponse callback);
  
  public interface LoginResponse extends GenericResponse {
    public abstract void success();
    public abstract void invalid(JSONObject errors);
  }
}