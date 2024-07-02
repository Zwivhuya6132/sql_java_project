package online_banking_system.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A utility class for generating various reports related to customers and their accounts.
 */
public class ReportGenerator {

    /**
     * Checks if a customer ID is valid by counting how many times it appears in the Customers table.
     *
     * @param connection The database connection to use.
     * @param customerId The customer ID to validate.
     * @return true if the customer ID exists in the database, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    public static boolean isCustomerIdValid(Connection connection, int customerId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Customers WHERE customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean isValid = resultSet.getInt(1) > 0;
                    System.out.println("isCustomerIdValid: " + isValid);
                    return isValid;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves and formats the address of a customer using their ID.
     *
     * @param connection The database connection to use.
     * @param customerId The customer ID whose address is to be retrieved.
     * @return The formatted address of the customer.
     * @throws SQLException If a database access error occurs.
     */
    public static String getCustomerAddress(Connection connection, int customerId) throws SQLException {
        String query = "SELECT first_name, last_name, street, house_number, zip_code, city, country " +
                "FROM Customers WHERE customer_id = ?";
        StringBuilder address = new StringBuilder();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    address.append("Customer Address:\n")
                            .append("Name: ").append(resultSet.getString("first_name")).append(" ").append(resultSet.getString("last_name")).append("\n")
                            .append("Address: ").append(resultSet.getString("house_number")).append(" ").append(resultSet.getString("street"))
                            .append(", ").append(resultSet.getString("zip_code")).append(" ").append(resultSet.getString("city"))
                            .append(", ").append(resultSet.getString("country")).append("\n");
                }
            }
        }
        System.out.println("getCustomerAddress:\n" + address.toString());
        return address.toString();
    }

    /**
     * Calculates the total balance of all accounts that belong to a customer.
     *
     * @param connection The database connection to use.
     * @param customerId The customer ID whose total balance is to be calculated.
     * @return The total balance of the customer's accounts.
     * @throws SQLException If a database access error occurs.
     */
    public static double getTotalBalance(Connection connection, int customerId) throws SQLException {
        String query = "SELECT SUM(a.current_balance) AS total_balance " +
                "FROM Accounts a WHERE a.customer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    double totalBalance = resultSet.getDouble("total_balance");
                    System.out.println("getTotalBalance: " + totalBalance);
                    return totalBalance;
                }
            }
        }
        return 0.0;
    }

    /**
     * Retrieves and formats transactions from checking accounts for a specific customer.
     * Displays the account number, date, amount, and transaction type of each transaction.
     *
     * @param connection The database connection to use.
     * @param customerId The customer ID whose checking account transactions are to be retrieved.
     * @return A formatted string listing the checking account transactions.
     * @throws SQLException If a database access error occurs.
     */
    public static String getCheckingAccountTransactions(Connection connection, int customerId) throws SQLException {
        String query = "SELECT a.account_number, t.date, t.amount, t.transaction_type " +
                "FROM Accounts a " +
                "JOIN Transactions t ON a.account_number = t.account_number " +
                "WHERE a.customer_id = ? AND a.account_type = 'CHECKING' " +
                "ORDER BY t.date";
        StringBuilder transactions = new StringBuilder("Checking Account Transactions:\n");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.append("Account Number: ").append(resultSet.getString("account_number")).append("\n")
                            .append("Date: ").append(resultSet.getDate("date")).append("\n")
                            .append("Amount: $").append(resultSet.getDouble("amount")).append("\n")
                            .append("Transaction Type: ").append(resultSet.getString("transaction_type")).append("\n")
                            .append("----\n");
                }
            }
        }
        System.out.println("getCheckingAccountTransactions:\n" + transactions.toString());
        return transactions.toString();
    }
}
