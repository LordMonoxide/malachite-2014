package malachite.engine.gateways;

import malachite.engine.exceptions.AccountException;
import malachite.engine.models.User;

public interface AccountGatewayInterface {
  public User login(String email, String password) throws AccountException, Exception;
}