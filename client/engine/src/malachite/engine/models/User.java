package malachite.engine.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import malachite.engine.exceptions.AccountException;
import malachite.engine.gateways.AccountGatewayInterface;

public abstract class User {
  private final User _this = this;
  
  private final AccountGatewayInterface _gateway;
  
  public final Characters characters = new Characters();
  public final String email;
  
  public User(AccountGatewayInterface gateway, String email) {
    _gateway = gateway;
    
    this.email = email;
  }
  
  public final class Characters implements Iterable<Character> {
    private List<Character> _chars;
    
    public Character get(int index) throws AccountException, Exception {
      lazyLoad();
      return _chars.get(index);
    }
    
    public Character add(String name) throws AccountException, Exception {
      Character c = _gateway.createCharacter(name);
      _chars.add(c);
      return c;
    }
    
    public void refresh() throws AccountException, Exception {
      _chars = new ArrayList<>(Arrays.asList(_gateway.getCharacters(_this)));
    }
    
    private void lazyLoad() throws AccountException, Exception {
      if(_chars == null) {
        refresh();
      }
    }
    
    @Override public Iterator<Character> iterator() {
      try {
        lazyLoad();
      } catch(Exception e) {
        throw new RuntimeException(e);
      }
      
      return new Iterator<Character>() {
        private int index = 0;
        
        @Override public boolean hasNext() {
          return _chars != null && index < _chars.size();
        }
        
        @Override public Character next() {
          return _chars.get(index++);
        }
        
        @Override public void remove(){
          throw new UnsupportedOperationException();
        }
      };
    }
  }
}