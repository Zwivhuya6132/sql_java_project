-- calculating the total balance of a customer by customer_id

SELECT c.customer_id, c.first_name, c.last_name, SUM(a.current_balance) AS total_balance
FROM Customers c
JOIN Accounts a ON c.customer_id = a.customer_id
WHERE c.customer_id = ?
GROUP BY c.customer_id, c.first_name, c.last_name
