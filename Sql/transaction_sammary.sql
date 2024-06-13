-- get the transaction summary of a customer

SELECT a.account_type, COUNT(t.transaction_id) AS transaction_count, SUM(t.amount) AS total_amount
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id
JOIN Transactions t ON a.account_number = t.account_number
WHERE c.customer_id = ?
GROUP BY a.account_type
