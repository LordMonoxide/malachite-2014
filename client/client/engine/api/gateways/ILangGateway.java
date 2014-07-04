package api.gateways;

import java.util.Map;

import api.Future;
import api.IErrorResponse;
import api.Routes;

public interface ILangGateway {
  public Future lang(Routes route, LangResponse callback);
  
  public interface LangResponse extends IErrorResponse {
    public void success(Map<String, String> lang);
  }
}