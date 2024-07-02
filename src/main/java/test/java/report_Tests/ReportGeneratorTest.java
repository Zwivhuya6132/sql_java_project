package test.java.report_Tests;

import online_banking_system.reports.ReportGenerator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import online_banking_system.customer_data.CustomerData;
import test.java.database_connection.ConnectionTest;

import java.sql.SQLException;

public class ReportGeneratorTest extends ConnectionTest {

    // This method provides a list of customer IDs for the tests.
    // Using a DataProvider is efficient and avoids the performance issues encountered with a scanner.
    @DataProvider(name = "customerIds")
    public Object[][] customerIds() {
        return CustomerData.customerIds();
    }

    // This test checks if the customer ID is valid.
    // Ensuring that customer IDs are valid helps us confirm that the data in the database is correct.
    @Test(dataProvider = "customerIds")
    public void testIsCustomerIdValid(int customerId) throws SQLException {
        boolean isValid = ReportGenerator.isCustomerIdValid(connection, customerId);
        Assert.assertTrue(isValid, "Customer ID " + customerId + " should be valid.");
    }

    // This test checks if we can correctly retrieve a customer's address.
    // Verifying the address ensures that our system can correctly fetch and display customer details.
    @Test(dataProvider = "customerIds")
    public void testGetCustomerAddress(int customerId) throws SQLException {
        String address = ReportGenerator.getCustomerAddress(connection, customerId);
        Assert.assertNotNull(address, "Address should not be null");
        Assert.assertTrue(address.contains("Customer Address:"), "Output should contain 'Customer Address:'");
    }

    // This test checks if the total balance for a customer is non-negative.
    // This ensures that our calculations for account balances are correct and there are no errors in the data.
    @Test(dataProvider = "customerIds")
    public void testGetTotalBalance(int customerId) throws SQLException {
        double totalBalance = ReportGenerator.getTotalBalance(connection, customerId);
        Assert.assertTrue(totalBalance >= 0, "Total balance should be non-negative");
    }

    // This test checks if we can correctly retrieve checking account transactions for a customer.
    // Verifying transactions ensures that our system can correctly log and display financial activities.
    @Test(dataProvider = "customerIds")
    public void testGetCheckingAccountTransactions(int customerId) throws SQLException {
        String transactions = ReportGenerator.getCheckingAccountTransactions(connection, customerId);
        Assert.assertNotNull(transactions, "Transactions should not be null");
        Assert.assertTrue(transactions.contains("Checking Account Transactions:"), "Output should contain 'Checking Account Transactions:'");
    }
}
