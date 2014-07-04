package api.responses;

import java.util.Map;

public interface ILangResponse extends IErrorResponse {
  public void success(Map<String, String> lang);
}