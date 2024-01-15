/*
 * DB
 * 
 * Copyright 2024 Bugs Bunny
 */
package gr.aueb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages database connections for the "bugsbunny" database.
 * 
 * The class provides methods to establish and close connections to the database
 * server.
 * It utilizes the MySQL JDBC driver to interact with the database.
 * The connection settings, including the database name, server address,
 * username, and password,
 * are defined as constants.
 * 
 * @version 1.8 released on 15th January 2024
 * @author Μαρκέλλα Χάσικου και Άγγελος Λαγός
 */

public class DB implements AutoCloseable {
	// Database connection settings, change dbName, dbUsername, dbPassword
	/** The address of the database server. */
	private final String dbServer = "localhost";
	/** The port number for the database server. */
	private final String dbServerPort = "3306";
	/** The name of the database. */
	private final String dbName = "bugsbunny";
	/** The username for connecting to the database. */
	private final String dbUsername = "root";
	/** The password for connecting to the database. */
	private final String dbPassword = "120032003BbMp4";
	/** The database connection object. */
	private Connection con = null;

	/**
	 * Establishes a connection with the database.
	 *
	 * @return The initialized database connection.
	 * @throws Exception if there is an error during the connection process.
	 */
	public Connection getConnection() throws Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			throw new Exception("MySQL Driver error: " + e.getMessage());
		}
		try {
			con = DriverManager.getConnection("jdbc:mysql://"
					+ dbServer + ":" + dbServerPort + "/" + dbName, dbUsername, dbPassword);
			return con;
		} catch (Exception e) {
			// throw Exception if any error occurs
			throw new Exception("Could not establish connection with the Database Server: "
					+ e.getMessage());
		}
	} // End of getConnection

	/**
	 * Closes the database connection.
	 *
	 * @throws SQLException if there is an error during the closing process.
	 */
	public void close() throws SQLException {
		try {
			if (con != null)
				con.close(); // close the connection to the database to end the database session
		} catch (SQLException e) {
			throw new SQLException("Could not close connection with the Database Server: "
					+ e.getMessage());
		}
	}
}
