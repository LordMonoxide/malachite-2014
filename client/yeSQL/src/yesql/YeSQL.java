package yesql;

import java.util.ArrayList;

public class YeSQL {
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
      protected final ArrayList<Where<?>> _where = new ArrayList<>();
      
      private Query(String type) {
        _type = type;
      }
      
      protected String buildWheres() {
        StringBuilder sql = new StringBuilder();
        for(int i = 0; i < _where.size(); i++) {
          if(i == 0) {
            sql.append(" WHERE ");
          } else {
            sql.append(" AND ");
          }
          
          sql.append(_where.get(i).build());
          
          if(i + 1 < _where.size()) {
            sql.append(',');
          }
        }
        
        return sql.toString();
      }
      
      public abstract String build();
    }
    
    public class Select extends Query {
      private final String[] _columns;
      private int _limit;
      
      private Select(String... columns) {
        super("SELECT");
        _columns = columns;
      }
      
      public Where<Select> where(String column) {
        Where<Select> where = new Where<>(this, column);
        _where.add(where);
        return where;
      }
      
      public Select limit(int limit) {
        _limit = limit;
        return this;
      }
      
      @Override public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append(_type).append(' ').append(String.join(",", _columns)).append(" FROM ").append(_name);
        
        if(_where.size() != 0) {
          sql.append(buildWheres());
        }
        
        if(_limit != 0) {
          sql.append(" LIMIT ").append(_limit);
        }
        
        return sql.append(';').toString();
      }
    }
    
    public class Where<T extends Query> {
      private final T _query;
      private final String _column;
      private String _op;
      private String _value;
      
      private Where(T query, String column) {
        _query  = query;
        _column = column;
      }
      
      public T equals(String value) {
        _op    = "=";
        _value = '\'' + value + '\'';
        return _query;
      }
      
      public T equals(Number value) {
        _op    = "=";
        _value = value.toString();
        return _query;
      }
      
      private String build() {
        return _column + _op + _value;
      }
    }
  }
}