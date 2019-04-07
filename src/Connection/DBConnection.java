package Connection;

import java.sql.*;



public class DBConnection {
	
	private static final String jdbcURL = "jdbc:mysql://localhost:3306/WolfHospital"; // Using SERVICE_NAME

	// Update your user and password info here!

	private static final String user = "root";
	private static final String password = "root@mysql";
	
	
	public static Connection ConnectDB() throws ClassNotFoundException, SQLException {

		Class.forName("com.mysql.jdbc.Driver");
        Connection conn = null;
        
        conn = DriverManager.getConnection(jdbcURL, user, password);
        
        return conn;
	}

	static void close(Connection conn) {
		if(conn != null) {
        try { conn.close(); } catch(Throwable whatever) {}
    }
	}
}

