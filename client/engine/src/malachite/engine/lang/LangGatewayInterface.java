package malachite.engine.lang;

public interface LangGatewayInterface {
  public Lang get() throws Exception;
  public Lang get(String name) throws Exception;
}