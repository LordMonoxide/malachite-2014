package api;

import io.netty.handler.codec.http.HttpMethod;

public class Routes {
  public final String route;
  public final HttpMethod method;
  
  private Routes(String route, HttpMethod method) {
    this.route  = route;
    this.method = method;
  }
  
  public static final class Lang {
    public static final Routes App  = new Routes("/lang/app" , HttpMethod.GET); //$NON-NLS-1$
    public static final Routes Menu = new Routes("/lang/menu", HttpMethod.GET); //$NON-NLS-1$
    
    private Lang() { }
  }
  
  public static final class Auth {
    public static final Routes Login = new Routes("/auth/login", HttpMethod.POST); //$NON-NLS-1$
    
    private Auth() { }
  }
}