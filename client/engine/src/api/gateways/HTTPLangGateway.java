package api.gateways;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import api.Future;
import api.HTTP;
import api.Routes;
import api.responses.ILangResponse;

public class HTTPLangGateway implements ILangGateway {
  @Override public Future lang(Routes route, ILangResponse callback) {
    Future f = new Future();
    
    HTTP.dispatch(route, resp -> {
      try {
        if(resp.succeeded()) {
          JSONObject j = resp.toJSON();
          
          Map<String, String> lang = new HashMap<>();
          
          for(String key : j.keySet()) {
            lang.put(key, j.getString(key));
          }
          
          callback.success(lang);
        } else {
          callback.error(resp.toString());
        }
      } catch(JSONException e) {
        callback.jsonError(resp.toString(), e);
      }
      
      f.complete();
    });
    
    return f;
  }
}