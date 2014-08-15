package malachite.engine.providers;

import com.mastfrog.netty.http.client.HttpClient;
import com.mastfrog.netty.http.client.HttpClientBuilder;

import malachite.engine.gateways.AccountGatewayInterface;
import malachite.engine.gateways.HTTPAccountGateway;

public class HTTPGatewayProvider implements GatewayProviderInterface {
  private AccountGatewayInterface _account;
  
  private HttpClient _http;
  
  public HTTPGatewayProvider() {
    _http = new HttpClientBuilder().build();
  }
  
  @Override public AccountGatewayInterface accountGateway() {
    if(_account == null) { _account = new HTTPAccountGateway(_http); }
    return _account;
  }
}