package api;

import net.http.Request;
import net.http.Response;
import io.netty.handler.codec.http.HttpHeaders;

import java.net.URISyntaxException;
import java.util.Map;

import org.json.JSONObject;

import api.responses.IGenericResponse;

public final class HTTP {
  private HTTP() { }
  
  private static final String APPLICATION_JSON = "application/json"; //$NON-NLS-1$
  
  private static final String NOT_AUTHED_SHOW          = "show";     //$NON-NLS-1$
  private static final String NOT_AUTHED_SHOW_LOGIN    = "login";    //$NON-NLS-1$
  
  public static void dispatch(Routes route, Request.Callback resp) {
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
  
  public static void checkGeneric(Response resp, IGenericResponse cb) {
    if(resp.response().status().code() == 401) {
      JSONObject j = resp.toJSON();
      switch(j.getString(NOT_AUTHED_SHOW)) {
        case NOT_AUTHED_SHOW_LOGIN: cb.loginRequired(); break;
        default: cb.error(resp.toString());
      }
    } else {
      cb.error(resp.toString());
    }
  }
}