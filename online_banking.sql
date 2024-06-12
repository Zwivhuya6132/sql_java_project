CREATE TABLE Customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE NOT NULL,
    street VARCHAR(100) NOT NULL,
    house_number VARCHAR(10) NOT NULL,
    zip_code VARCHAR(10) NOT NULL,
    city VARCHAR(50) NOT NULL,
    country VARCHAR(50) NOT NULL,
    UNIQUE (first_name, last_name, date_of_birth)
);

CREATE TABLE Accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type ENUM('CHECKING', 'SAVINGS') NOT NULL,
    current_balance DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES Customers(customer_id)
);

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL,
    date DATE NOT NULL,
    transaction_type ENUM('WITHDRAWAL', 'DEPOSIT') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (account_number) REFERENCES Accounts(account_number)
);
