package malachite.engine.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.gateways.AccountGatewayInterface;

public class JDBCUser extends User {
  public static final String TABLE       = "users";    //$NON-NLS-1$
  public static final String DB_ID       = "id";       //$NON-NLS-1$
  public static final String DB_EMAIL    = "email";    //$NON-NLS-1$
  public static final String DB_PASSWORD = "password"; //$NON-NLS-1$
  
  public final int id;
  
  public JDBCUser(AccountGatewayInterface gateway, ResultSet r) throws SQLException {
    this(gateway, r.getInt(DB_ID), r.getString(DB_EMAIL));
  }
  
  public JDBCUser(AccountGatewayInterface gateway, int id, String email) {
    super(gateway, email);
    this.id = id;
  }
}