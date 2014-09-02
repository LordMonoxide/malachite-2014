package malachite.engine.lang;

import malachite.engine.conf.Conf;

public class ConfLang extends Lang {
  private final Conf _conf;
  
  ConfLang(Conf conf) {
    _conf = conf;
  }
  
  @Override public String get(String key) {
    Object obj = _conf.get(key);
    
    if(obj == null) {
      return key;
    }
    
    return _conf.get(key).toString();
  }
}