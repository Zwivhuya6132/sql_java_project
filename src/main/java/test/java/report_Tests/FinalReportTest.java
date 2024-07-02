package test.java.report_Tests;

import online_banking_system.reports.FinalReport;
import online_banking_system.customer_data.CustomerData;
import online_banking_system.database_connection.ConnectionManager;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.java.database_connection.ConnectionTest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class FinalReportTest extends ConnectionTest {

    // Data provider for customer IDs
    @DataProvider(name = "customerIds")
    public Object[][] customerIds() {
        return CustomerData.customerIds();
    }

    // Test to verify that the total transactions for a specific account (by ID) are zero.
    @Test(dataProvider = "customerIds")
    public void testTotalTransactionsForAccountIsZero(int customerId) throws SQLException {
        // Fetch account IDs for the given customer ID
        List<Integer> accountIds = FinalReport.getAccountIdsForCustomer(connection, customerId);

        // Iterate over the account IDs and check transactions for each
        for (int accountId : accountIds) {
            double totalTransactions = FinalReport.getTotalTransactionsForAccount(connection, accountId);
            Assert.assertEquals(totalTransactions, 0.0, "Total transactions for the account should be 0.");
        }
    }

    // Test to verify the total number of customers in the database.
    // Assumes there should be exactly 2 customers.
    @Test
    public void testTotalNumberOfCustomers() throws SQLException {
        int totalCustomers = FinalReport.getTotalNumberOfCustomers(connection);
        Assert.assertEquals(totalCustomers, 2, "Total number of customers should be 2.");
    }

    // Test to ensure that a customer named Sarah has at least two accounts.
    @Test
    public void testSarahHasTwoAccounts() throws SQLException {
        int accountCount = FinalReport.getAccountCountForCustomerByName(connection, "Sarah");
        Assert.assertTrue(accountCount >= 2, "Sarah should have two or more accounts.");
    }

    // Test to check if a given customer ID is valid.
    @Test(dataProvider = "customerIds")
    public void testIsCustomerIdValid(int customerId) throws SQLException {
        boolean isValid = FinalReport.isCustomerIdValid(connection, customerId);
        Assert.assertTrue(isValid, "Customer ID " + customerId + " should be valid.");
    }

    // Test to print the address of a customer based on their ID.
    @Test(dataProvider = "customerIds")
    public void testPrintCustomerAddress(int customerId) throws SQLException {
        FinalReport.printCustomerAddress(connection, customerId);
    }

    // Test to print the total balance for a customer based on their ID.
    @Test(dataProvider = "customerIds")
    public void testPrintTotalBalance(int customerId) throws SQLException {
        FinalReport.printTotalBalance(connection, customerId);
    }

    // Test to print the checking account transactions for a customer based on their ID.
    @Test(dataProvider = "customerIds")
    public void testPrintCheckingAccountTransactions(int customerId) throws SQLException {
        FinalReport.printCheckingAccountTransactions(connection, customerId);
    }
}
