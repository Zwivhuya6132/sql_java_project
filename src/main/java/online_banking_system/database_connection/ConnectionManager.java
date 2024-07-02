package online_banking_system.database_connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    // JDBC URL to connect to the MySQL database
    private static final String URL = "jdbc:mysql://rfd.h.filess.io:3307/onlineBanking_leatherhit";
    private static final String USER = "onlineBanking_leatherhit";
    private static final String PASSWORD = "8e430c77ace31ccebb18fc21288f8f0c12bb1ba1";

    // Connection instance to manage the database connection
    private static Connection connection;

    // Private constructor to prevent instantiation of the ConnectionManager class
    private ConnectionManager() {
        // Private constructor to prevent instantiation, ensuring all access is through static methods
    }

    // Retrieves a connection to the MySQL database
    public static Connection getConnection() throws SQLException {
        // If connection is null or closed, establish a new connection
        if (connection == null || connection.isClosed()) {
            try {
                // Load MySQL JDBC driver dynamically
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish connection using DriverManager
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                // Throw SQLException if MySQL JDBC driver is not found
                throw new SQLException("MySQL JDBC driver not found.", e);
            }
        }
        // Return the established connection
        return connection;
    }

    // Closes the database connection
    public static void closeConnection() throws SQLException {
        // If connection is not null and is not closed, close the connection
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
