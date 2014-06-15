package malachite.api;

import malachite.net.http.Request;
import malachite.net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public final class API {
  private API() { }
  
  private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$
  
  private static void dispatch(Route route, Request.Callback resp) {
    dispatch(route, null, resp);
  }
  
  private static void dispatch(Route route, Map<String, String> data, Request.Callback resp) {
    Request r = new Request();
    r.setMethod(route.method);
    
    try {
      r.setRoute(route.route);
    } catch(URISyntaxException e) { }
    
    r.addHeader(HttpHeaders.Names.ACCEPT, APPLICATION_JSON);
    r.setData(data);
    r.dispatch(resp);
  }
  
  public static Future lang(Route route, LangResponse cb) {
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
    public abstract void securityRequired(String[] question);
  }
  
  public static abstract class LangResponse {
    public abstract void success(Map<String, String> lang);
    public abstract void error(Response r);
    public abstract void jsonError(Response r, JSONException e);
  }
  
  public static class Route {
    public final String route;
    public final HttpMethod method;
    
    private Route(String route, HttpMethod method) {
      this.route  = route;
      this.method = method;
    }
    
    public static final class Lang {
      public static final Route App  = new Route("/lang/app" , HttpMethod.GET); //$NON-NLS-1$
      
      private Lang() { }
    }
  }
}