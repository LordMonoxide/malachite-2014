package malachite.api;

import java.util.Map;

import org.json.JSONException;

import malachite.net.http.Response;

public class Lang<T> {
  public static final Lang<AppKeys>  App  = new Lang<>();
  
  public static void load() {
    System.out.println("Getting lang..."); //$NON-NLS-1$
    
    Future.await(
      API.lang(API.Route.Lang.App, new API.LangResponse() {
        @Override public void success(Map<String, String> lang) {
          App._lang = lang;
        }
        
        @Override public void error(Response r) {
          System.err.println(r.content());
        }
        
        @Override public void jsonError(Response r, JSONException e) {
          System.err.println("JSON encoding error getting app lang:\n" + e + '\n' + r.content()); //$NON-NLS-1$
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
}