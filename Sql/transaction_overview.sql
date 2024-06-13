--  return all transactions for a given customer_id

SELECT a.account_number, t.date, t.amount, t.transaction_type
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id
JOIN Transactions t ON a.account_number = t.account_number
WHERE c.customer_id = ? AND a.account_type = 'CHECKING'
ORDER BY t.date
