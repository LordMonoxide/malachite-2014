package api.gateways;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import api.HTTP;
import api.Future;
import api.Routes;
import api.models.User;

public class HTTPAuthGateway implements IAuthGateway {
  @Override public Future login(String email, String password, LoginResponse callback) {
    Future f = new Future();
    
    Map<String, String> data = new HashMap<>();
    data.put(User.KEY_EMAIL,    email);
    data.put(User.KEY_PASSWORD, password);
    
    HTTP.dispatch(Routes.Auth.Login, data, resp -> {
      try {
        if(resp.succeeded()) {
          callback.success();
        } else {
          if(resp.response().getStatus().code() == 409) {
            callback.invalid(resp.toJSON());
          } else {
            HTTP.checkGeneric(resp, callback);
          }
        }
      } catch(JSONException e) {
        callback.jsonError(resp, e);
      }
      
      f.complete();
    });
    
    return f;
  }
}