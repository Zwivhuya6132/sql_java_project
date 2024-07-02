package online_banking_system.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinalReport {

    /**
     * Retrieves the total number of customers from the database.
     *
     * @param connection Connection to the database
     * @return Total number of customers
     * @throws SQLException If a database access error occurs
     */
    public static int getTotalNumberOfCustomers(Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) AS total_customers FROM Customers")) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("total_customers");
        }
    }

    /**
     * Retrieves the number of accounts for a customer based on first name.
     *
     * @param connection Connection to the database
     * @param firstName  First name of the customer
     * @return Number of accounts for the customer
     * @throws SQLException If a database access error occurs
     */
    public static int getAccountCountForCustomerByName(Connection connection, String firstName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT COUNT(*) AS account_count FROM Accounts " +
                        "JOIN Customers ON Accounts.customer_id = Customers.customer_id " +
                        "WHERE Customers.first_name = ?")) {
            statement.setString(1, firstName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("account_count");
        }
    }

    /**
     * Retrieves the total transactions amount for a specific account.
     *
     * @param connection Connection to the database
     * @param accountId  Account ID for which transactions are calculated
     * @return Total transactions amount for the account
     * @throws SQLException If a database access error occurs
     */
    public static double getTotalTransactionsForAccount(Connection connection, int accountId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT SUM(amount) AS total_transactions FROM Transactions WHERE account_id = ?")) {
            statement.setInt(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("total_transactions");
        }
    }

    /**
     * Checks if a customer ID is valid by querying the database.
     *
     * @param connection Connection to the database
     * @param customerId Customer ID to validate
     * @return True if the customer ID exists, false otherwise
     * @throws SQLException If a database access error occurs
     */
    public static boolean isCustomerIdValid(Connection connection, int customerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * Prints the customer's address details based on the customer ID.
     *
     * @param connection Connection to the database
     * @param customerId Customer ID for which address is retrieved
     * @throws SQLException If a database access error occurs
     */
    public static void printCustomerAddress(Connection connection, int customerId) throws SQLException {
        String query = "SELECT first_name, last_name, street, house_number, zip_code, city, country " +
                "FROM Customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Customer Address:");
                System.out.println("Name: " + resultSet.getString("first_name") + " " + resultSet.getString("last_name"));
                System.out.println("Address: " + resultSet.getString("house_number") + " " + resultSet.getString("street") +
                        ", " + resultSet.getString("zip_code") + " " + resultSet.getString("city") +
                        ", " + resultSet.getString("country"));
            }
        }
    }

    /**
     * Prints the total balance of all accounts for a customer.
     *
     * @param connection Connection to the database
     * @param customerId Customer ID for which total balance is calculated
     * @throws SQLException If a database access error occurs
     */
    public static void printTotalBalance(Connection connection, int customerId) throws SQLException {
        String query = "SELECT SUM(a.current_balance) AS total_balance " +
                "FROM Accounts a WHERE a.customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Total Balance: $" + resultSet.getDouble("total_balance"));
            }
        }
    }

    /**
     * Prints the transactions details for checking accounts of a customer.
     *
     * @param connection Connection to the database
     * @param customerId Customer ID for which checking account transactions are retrieved
     * @throws SQLException If a database access error occurs
     */
    public static void printCheckingAccountTransactions(Connection connection, int customerId) throws SQLException {
        String query = "SELECT a.account_number, t.date, t.amount, t.transaction_type " +
                "FROM Accounts a " +
                "JOIN Transactions t ON a.account_number = t.account_number " +
                "WHERE a.customer_id = ? AND a.account_type = 'CHECKING' " +
                "ORDER BY t.date";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            ResultSet resultSet = statement.executeQuery();
            System.out.println("Checking Account Transactions:");
            while (resultSet.next()) {
                System.out.println("Account Number: " + resultSet.getString("account_number"));
                System.out.println("Date: " + resultSet.getDate("date"));
                System.out.println("Amount: $" + resultSet.getDouble("amount"));
                System.out.println("Transaction Type: " + resultSet.getString("transaction_type"));
                System.out.println("----");
            }
        }
    }

    /**
     * Retrieves the list of account IDs associated with a customer.
     *
     * @param connection Connection to the database
     * @param customerId Customer ID for which account IDs are retrieved
     * @return List of account IDs
     * @throws SQLException If a database access error occurs
     */
    public static List<Integer> getAccountIdsForCustomer(Connection connection, int customerId) throws SQLException {
        // Initialize a list to store account IDs
        List<Integer> accountIds = new ArrayList<>();

        // SQL query to fetch account IDs for the given customer ID
        String query = "SELECT account_id FROM Accounts WHERE customer_id = ?";

        // Use try-with-resources to ensure resources are closed after use
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set the customer ID in the prepared statement
            statement.setInt(1, customerId);

            // Execute the query and get the result set
            ResultSet resultSet = statement.executeQuery();

            // Iterate through the result set and add each account ID to the list
            while (resultSet.next()) {
                accountIds.add(resultSet.getInt("account_id"));
            }
        }

        // Return the list of account IDs
        return accountIds;
    }
}
