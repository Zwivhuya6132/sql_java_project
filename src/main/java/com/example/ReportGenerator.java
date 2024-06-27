package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ReportGenerator {

    public static void main(String[] args) {

        
        // Database connection details
        String hostname = "rfd.h.filess.io";
        String database = "onlineBanking_leatherhit";
        String port = "3307";
        String username = "onlineBanking_leatherhit";
        String password = "8e430c77ace31ccebb18fc21288f8f0c12bb1ba1";

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();

        try  {

            
            
            Connection connection = DriverManager.getConnection(hostname, database, port, username, password);
            // Check if the customer ID is valid
            if (isCustomerIdValid(connection, customerId)) {
                // Generate reports
                printCustomerAddress(connection, customerId);
                printTotalBalance(connection, customerId);
                printCheckingAccountTransactions(connection, customerId);
            } else {
                System.out.println("Invalid customer ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean isCustomerIdValid(Connection connection, int customerId) throws SQLException {
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

    private static void printCustomerAddress(Connection connection, int customerId) throws SQLException {
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

    private static void printTotalBalance(Connection connection, int customerId) throws SQLException {
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

    private static void printCheckingAccountTransactions(Connection connection, int customerId) throws SQLException {
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
}
