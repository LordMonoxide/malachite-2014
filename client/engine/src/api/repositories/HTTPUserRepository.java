package api.repositories;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import api.Future;
import api.HTTP;
import api.Routes;
import api.models.User;
import api.responses.IModelGetResponse;

public class HTTPUserRepository implements IUserRepository {
  @Override public Future get(String email, IModelGetResponse<User> callback) {
    Future f = new Future();
    
    Map<String, String> data = new HashMap<>();
    data.put(User.KEY_EMAIL, email);
    
    HTTP.dispatch(Routes.Auth.Login, data, resp -> {
      try {
        if(resp.succeeded()) {
          callback.success();
        } else {
          if(resp.response().status().code() == 409) {
            callback.invalid(resp.toJSON());
          } else {
            HTTP.checkGeneric(resp, callback);
          }
        }
      } catch(JSONException e) {
        callback.jsonError(resp.toString(), e);
      }
      
      f.complete();
    });
    
    return f;
  }
}