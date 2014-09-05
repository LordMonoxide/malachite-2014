package yesql;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class YeSQL {
  public Table table(String name) {
    return new Table(name);
  }
  
  public final class Table {
    private final String _name;
    
    private Table(String name) {
      _name = name;
    }
    
    public Insert insert() {
      return new Insert();
    }
    
    public Select select() {
      return select("*");
    }
    
    public Select select(String... columns) {
      return new Select(false, columns);
    }
    
    public Select count() {
      return new Select(true, "*");
    }
    
    public Delete delete() {
      return new Delete();
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
    
    public class Insert {
      private Map<String, String> _values = new LinkedHashMap<>();
      
      private Insert() { }
      
      public Insert value(String column, String value) {
        _values.put(column, '\'' + value + '\'');
        return this;
      }
      
      public Insert value(String column, Number value) {
        _values.put(column, value.toString());
        return this;
      }
      
      public Insert value(String column) {
        _values.put(column, "?");
        return this;
      }
      
      public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(_name).append('(');
        
        int i = 0;
        for(String column : _values.keySet()) {
          sql.append(column);
          if(++i != _values.size()) {
            sql.append(',');
          }
        }
        
        sql.append(") VALUES (");
        
        i = 0;
        for(String value : _values.values()) {
          sql.append(value);
          if(++i != _values.size()) {
            sql.append(',');
          }
        }
        
        return sql.append(");").toString();
      }
    }
    
    public class Select extends Query {
      private final boolean _count;
      private final String[] _columns;
      private int _limit;
      
      private Select(boolean count, String... columns) {
        super("SELECT");
        _count   = count;
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
        sql.append(_type).append(' ');
        
        if(_count) {
          sql.append("COUNT(").append(String.join(",", _columns)).append(')');
        } else {
          sql.append(String.join(",", _columns));
        }
        
        sql.append(" FROM ").append(_name);
        
        if(_where.size() != 0) {
          sql.append(buildWheres());
        }
        
        if(_limit != 0) {
          sql.append(" LIMIT ").append(_limit);
        }
        
        return sql.append(';').toString();
      }
    }
    
    public class Delete extends Query {
      private Delete() {
        super("DELETE");
      }
      
      public Where<Delete> where(String column) {
        Where<Delete> where = new Where<>(this, column);
        _where.add(where);
        return where;
      }
      
      @Override public String build() {
        StringBuilder sql = new StringBuilder();
        sql.append(_type).append(" FROM ").append(_name);
        
        if(_where.size() != 0) {
          sql.append(buildWheres());
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
      
      public T equals() {
        _op    = "=";
        _value = "?";
        return _query;
      }
      
      private String build() {
        return _column + _op + _value;
      }
    }
  }
}