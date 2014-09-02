package malachite.engine.lang;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import malachite.engine.conf.Conf;

public class ConfLangGateway implements LangGatewayInterface {
  private final Map<String, Lang> _lang = new HashMap<>();
  
  @Override public Lang get(String name) throws JSONException, IOException {
    Lang lang = _lang.get(name);
    
    if(lang == null) {
      lang = loadLang(name);
    }
    
    return lang;
  }
  
  private Lang loadLang(String name) throws JSONException, IOException {
    Lang lang = _lang.get(name);
    
    if(lang == null) {
      Conf conf = new Conf("../data/lang/" + name + ".conf");
      lang = new ConfLang(conf);
    }
    
    return lang;
  }
}