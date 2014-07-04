package api;

import net.http.Request;
import net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public final class HTTP {
  private HTTP() { }
  
  private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$
  
  private static final String NOT_AUTHED_SHOW          = "show";     //$NON-NLS-1$
  private static final String NOT_AUTHED_SHOW_LOGIN    = "login";    //$NON-NLS-1$
  
  private static void dispatch(Routes route, Request.Callback resp) {
    dispatch(route, null, resp);
  }
  
  public static void dispatch(Routes route, Map<String, String> data, Request.Callback resp) {
    Request r = new Request();
    r.setMethod(route.method);
    
    try {
      r.setRoute(route.route);
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.setData(data);
    r.dispatch(resp);
  }
  
  public static void checkGeneric(Response resp, GenericResponse cb) {
    if(resp.response().getStatus().code() == 401) {
      JSONObject j = resp.toJSON();
      switch(j.getString(NOT_AUTHED_SHOW)) {
        case NOT_AUTHED_SHOW_LOGIN: cb.loginRequired(); break;
        default: cb.error(resp);
      }
    } else {
      cb.error(resp);
    }
  }
  
  public static Future lang(Routes route, LangResponse cb) {
    Future f = new Future();
    
    dispatch(route, resp -> {
      try {
        if(resp.succeeded()) {
          JSONObject j = resp.toJSON();
          
          Map<String, String> lang = new HashMap<>();
          
          for(String key : j.keySet()) {
            lang.put(key, j.getString(key));
          }
          
          cb.success(lang);
        } else {
          cb.error(resp);
        }
      } catch(JSONException e) {
        cb.jsonError(resp, e);
      }
      
      f.complete();
    });
    
    return f;
  }
  
  public interface ErrorResponse {
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public interface GenericResponse extends ErrorResponse {
    public abstract void loginRequired();
  }
  
  public static abstract class LangResponse {
    public abstract void success(Map<String, String> lang);
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
}