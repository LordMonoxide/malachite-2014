package malachite.engine.gateways;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpHeaders;

import com.mastfrog.netty.http.client.HttpClient;
import com.mastfrog.netty.http.client.ResponseHandler;

public class HTTPAccountGateway implements AccountGatewayInterface {
  private final HttpClient _http;
  
  public HTTPAccountGateway(HttpClient http) {
    _http = http;
  }
  
  @Override public void login(String email, String password) {
    _http.put()
     .setURL("http://local.malachite.monoxidedesign.com/api/auth/login")
     .execute(new ResponseHandler<String>(String.class) {
       @Override protected void receive(HttpResponseStatus status, HttpHeaders headers, String response) {
         System.out.println("Here's the response: '" + response + '\'');
       }
     });
  }
}