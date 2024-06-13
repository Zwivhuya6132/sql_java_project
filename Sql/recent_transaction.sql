-- Get the 10 most recent transactions for a given customer

SELECT a.account_number, t.date, t.amount, t.transaction_type
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id
JOIN Transactions t ON a.account_number = t.account_number
WHERE c.customer_id = ?
ORDER BY t.date DESC
LIMIT 10
