package malachite.engine.gateways;

import malachite.engine.exceptions.AccountException;
import malachite.engine.models.Character;
import malachite.engine.models.User;

public interface AccountGatewayInterface {
  public User login(String email, String password) throws AccountException, Exception;
  public User register(String email, String password) throws AccountException, Exception;
  public Character[] getCharacters(User user) throws AccountException, Exception;
  public Character createCharacter(String name) throws AccountException, Exception;
}