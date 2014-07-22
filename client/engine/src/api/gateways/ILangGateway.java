package api.gateways;

import api.Future;
import api.Routes;
import api.responses.ILangResponse;

public interface ILangGateway {
  public Future lang(Routes route, ILangResponse callback);
}