package malachite.engine.lang;

import java.util.HashMap;
import java.util.Map;

public class ConfLangGateway implements LangGatewayInterface {
  private final Map<String, Lang> _lang = new HashMap<>();
  
  @Override public Lang get(String name) {
    Lang lang = _lang.get(name);
    
    if(lang == null) {
      lang = loadLang(name);
    }
    
    return lang;
  }
  
  private Lang loadLang(String name) {
    return null;
  }
}