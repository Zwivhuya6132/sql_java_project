package test.java.database_connection;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import online_banking_system.database_connection.ConnectionManager;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * Test class for testing the database connection using TestNG framework.
 */
public class ConnectionTest {

    protected Connection connection;

    /**
     * Set up method executed before the test class starts.
     * Establishes a database connection using ConnectionManager.
     *
     * @throws SQLException If a database access error occurs.
     */
    @BeforeClass
    public void setUp() throws SQLException {
        // Establish database connection
        connection = ConnectionManager.getConnection();

        // Assert that connection is not null (connection should be established successfully)
        Assert.assertNotNull(connection, "Database connection should be established.");
    }

    /**
     * Tear down method executed after the test class completes.
     * Closes the database connection using ConnectionManager.
     *
     * @throws SQLException If a database access error occurs.
     */
    @AfterClass
    public void tearDown() throws SQLException {
        // Close database connection
        ConnectionManager.closeConnection();
    }
}
