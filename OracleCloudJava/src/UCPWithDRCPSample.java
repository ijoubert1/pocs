import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

public class UCPWithDRCPSample {
  final static String DRCP_URL= "jdbc:oracle:thin:@"
  		+ "(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.sa-saopaulo-1.oraclecloud.com))(connect_data=(service_name=jksyowoppyulrjv_db202002081735_medium.atp.oraclecloud.com))(security=(ssl_server_cert_dn=\"CN=adb.sa-saopaulo-1.oraclecloud.com,OU=Oracle ADB SAOPAULO,O=Oracle Corporation,L=Redwood City,ST=California,C=US\")))";
  final static String DB_USER = "ADMIN";
  final static String DB_PASSWORD = "Adicjc18028o";
  final static String UCP_CONNFACTORY = "oracle.jdbc.pool.OracleDataSource";
  
 /*
  * The sample shows how to use DRCP with UCP. Make sure that correct  
  * connection URL is used and DRCP is enabled both on the server side 
  * and on the client side. 
  */
  static public void main(String args[]) throws SQLException {
    PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();
    pds.setConnectionFactoryClassName(UCP_CONNFACTORY);    
    pds.setUser(DB_USER);
    pds.setPassword(DB_PASSWORD);
    // Make sure that DRCP_URL has (SERVER=POOLED) specified
    pds.setURL(DRCP_URL);
    pds.setConnectionPoolName("DRCP_UCP_Pool");

    // Set UCP Properties
    pds.setInitialPoolSize(1);
    pds.setMinPoolSize(4);
    pds.setMaxPoolSize(20);

    // Get the Database Connection from Universal Connection Pool.
    try (Connection conn = pds.getConnection()) {
      System.out.println("\nConnection obtained from UniversalConnectionPool");
      // Perform a database operation
      doSQLWork(conn);
      System.out.println("Connection returned to the UniversalConnectionPool");
    }
  }

 /*
  * Displays system date (sysdate). 
  */
  public static void doSQLWork(Connection connection) throws SQLException {
    // Statement and ResultSet are auto-closable by this syntax
    try (Statement statement = connection.createStatement()) {
      try (ResultSet resultSet = statement
          .executeQuery("select SYSDATE from DUAL")) {
        while (resultSet.next())
          System.out.print("Today's date is " + resultSet.getString(1) + " ");
      }
    }
    System.out.println("\n");
  } 
}

