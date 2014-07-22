package api.gateways;

import api.Future;
import api.responses.ILoginResponse;

public interface IAuthGateway {
  public Future login(String email, String password, ILoginResponse callback);
}