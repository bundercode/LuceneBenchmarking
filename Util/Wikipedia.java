package Util;

import java.sql.*;

public class Wikipedia {

  Connection connect;

  public ResultSet getResult(String s) throws Exception {
    Statement statement = connect.createStatement();
    return statement.executeQuery(s);

  }

  public void closeConnection() throws Exception {
    connect.close();
  }

  public Wikipedia() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
     connect = DriverManager.getConnection("jdbc:mysql://softbase.ipfw.edu:3306/jwpl_tables?user=root&autoReconnect=true&tcpKeepAlive=true");//wikidb
    } catch (Exception e) {
      System.out.println(e.toString());
      e.printStackTrace();
    }
  }
}
