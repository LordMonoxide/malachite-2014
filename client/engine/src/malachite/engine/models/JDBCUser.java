package malachite.engine.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.gateways.AccountGatewayInterface;

public class JDBCUser extends User<Integer> {
  public JDBCUser(AccountGatewayInterface gateway, ResultSet r) throws SQLException {
    this(gateway, r.getInt(User.DB_ID), r.getString(User.DB_EMAIL));
  }
  
  public JDBCUser(AccountGatewayInterface gateway, int id, String email) {
    super(gateway, Integer.valueOf(id), email);
  }
}