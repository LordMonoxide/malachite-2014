package malachite.engine.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import malachite.engine.gateways.AccountGatewayInterface;

public class JDBCCharacter extends Character {
  public static final String TABLE      = "characters"; //$NON-NLS-1$
  public static final String DB_ID      = "id";         //$NON-NLS-1$
  public static final String DB_USER_ID = "user_id";    //$NON-NLS-1$
  public static final String DB_NAME    = "name";       //$NON-NLS-1$
  
  public final int  id;
  
  public JDBCCharacter(AccountGatewayInterface gateway, JDBCUser user, ResultSet r) throws SQLException {
    this(gateway, r.getInt(DB_ID), user, r.getString(DB_NAME));
  }
  
  public JDBCCharacter(AccountGatewayInterface gateway, int id, JDBCUser user, String name) {
    super(gateway, user, name);
    this.id   = id;
  }
}