package api;

import java.util.Map;

import org.json.JSONException;

import api.gateways.ILangGateway;

public class Lang<T> {
  public static final Lang<AppKeys>  App  = new Lang<>();
  public static final Lang<MenuKeys> Menu = new Lang<>();
  
  public static void load() {
    System.out.println("Getting lang..."); //$NON-NLS-1$
    
    ILangGateway lang = Instances.newLangGateway();
    
    Future.await(
      lang.lang(Routes.Lang.App, new ILangGateway.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          App._lang = lang;
        }
        
        @Override public void error(String source) {
          System.err.println(source);
        }
        
        @Override public void jsonError(String source, JSONException e) {
          System.err.println("JSON encoding error getting app lang:\n" + e + '\n' + source); //$NON-NLS-1$
        }
      }),
      
      lang.lang(Routes.Lang.Menu, new ILangGateway.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          Menu._lang = lang;
        }
        
        @Override public void error(String source) {
          System.err.println(source);
        }
        
        @Override public void jsonError(String source, JSONException e) {
          System.err.println("JSON encoding error getting app lang:\n" + e + '\n' + source); //$NON-NLS-1$
        }
      })
    );
  }
  
  private Map<String, String> _lang;
  
  private Lang() { }
  
  public String get(T key, String... substitute) {
    if(_lang == null) {
      System.err.println("Missing lang"); //$NON-NLS-1$
      return key.toString();
    }
    
    String lang = _lang.get(key.toString());
    
    if(lang == null) {
      System.err.println("Missing lang mapping for " + key); //$NON-NLS-1$
      return key.toString();
    }
    
    for(String s : substitute) {
      lang = lang.replaceFirst(":\\w+", s); //$NON-NLS-1$
    }
    
    return lang;
  }
  
  public enum AppKeys {
    TITLE("title"); //$NON-NLS-1$
    
    String _text;
    
    AppKeys(String text) {
      _text = text;
    }
    
    @Override public String toString() {
      return _text;
    }
  }
  
  public enum MenuKeys {
    LOGIN_TITLE   ("login.title"),    //$NON-NLS-1$
    LOGIN_EMAIL   ("login.email"),    //$NON-NLS-1$
    LOGIN_PASSWORD("login.password"), //$NON-NLS-1$
    LOGIN_LOGIN   ("login.login"),    //$NON-NLS-1$
    
    ERROR_ERROR   ("error.error"),    //$NON-NLS-1$
    ERROR_JSON    ("error.json");     //$NON-NLS-1$
    
    String _text;
    
    MenuKeys(String text) {
      _text = text;
    }
    
    @Override public String toString() {
      return _text;
    }
  }
}