package malachite.engine.gateways;

import malachite.engine.providers.JDBCGatewayProvider;

public class JDBCAccountGateway implements AccountGatewayInterface {
  private JDBCGatewayProvider _provider;
  
  public JDBCAccountGateway(JDBCGatewayProvider provider) {
    _provider = provider;
  }
  
  @Override public void login(String email, String password) {
    
  }
}