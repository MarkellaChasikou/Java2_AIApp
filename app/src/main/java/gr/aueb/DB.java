package gr.aueb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB implements AutoCloseable{
    //Database connection settings, change dbName, dbusername, dbpassword
	private final String dbServer = "localhost";
	private final String dbServerPort = "3306";
	private final String dbName = "bugsbunny";
	private final String dbusername = "root";
	private final String dbpassword = "120032003BbMp4";
	private Connection con = null;
	
	//Establishes a connection with the database, initializes and returns
	public Connection getConnection() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			throw new Exception("MySQL Driver error: " + e.getMessage());
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://" 
				+ dbServer + ":" + dbServerPort + "/" + dbName, dbusername, dbpassword);
			return con;
		} catch (Exception e) {
			// throw Exception if any error occurs
			throw new Exception("Could not establish connection with the Database Server: " 
				+ e.getMessage());
		}
	} // End of getConnection

	//Close database connection.
	public void close() throws SQLException {
		try {
			if (con != null)
				con.close(); // close the connection to the database to end database session
		} catch (SQLException e) {
			throw new SQLException("Could not close connection with the Database Server: " 
				+ e.getMessage());
		}
	} 
}






