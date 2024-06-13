-- Retrive the address of a customer by customer_id

SELECT first_name, last_name, street, house_number, zip_code, city, country
FROM Customers
WHERE customer_id = ?
