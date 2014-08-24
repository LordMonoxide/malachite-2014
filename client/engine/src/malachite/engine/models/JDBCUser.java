package malachite.engine.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCUser extends User<Integer> {
  public JDBCUser(ResultSet r) throws SQLException {
    this(r.getInt(User.DB_ID), r.getString(User.DB_EMAIL));
  }
  
  public JDBCUser(int id, String email) {
    super(Integer.valueOf(id), email);
  }
}