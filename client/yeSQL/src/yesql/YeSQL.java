package yesql;

public class YeSQL {
  public static void main(String[] args) {
    System.out.println(new YeSQL().table("users").select().build());
  }
  
  public Table table(String name) {
    return new Table(name);
  }
  
  public final class Table {
    private final String _name;
    
    private Table(String name) {
      _name = name;
    }
    
    public Select select() {
      return select("*");
    }
    
    public Select select(String... columns) {
      return new Select(columns);
    }
    
    public abstract class Query {
      protected final String _type;
      
      private Query(String type) {
        _type = type;
      }
      
      public abstract String build();
    }
    
    public class Select extends Query {
      protected final String[] _columns;
      
      private Select(String... columns) {
        super("SELECT");
        _columns = columns;
      }
      
      public SelectCount count() {
        return new SelectCount(_columns);
      }
      
      public SelectLimit limit(int limit) {
        return new SelectLimit(limit, _columns);
      }
      
      @Override public String build() {
        return _type + ' ' + String.join(",", _columns) + " FROM " + _name;
      }
    }
    
    public class SelectCount extends Select {
      private SelectCount(String... columns) {
        super(columns);
      }
      
      @Override public String build() {
        return _type + " COUNT(" + String.join(",", _columns) + ") FROM " + _name;
      }
    }
    
    public class SelectLimit extends Select {
      private final int _limit;
      
      private SelectLimit(int limit, String... columns) {
        super(columns);
        _limit = limit;
      }
      
      @Override public String build() {
        return _type + ' ' + String.join(",", _columns) + " FROM " + _name + " LIMIT " + _limit;
      }
    }
  }
}