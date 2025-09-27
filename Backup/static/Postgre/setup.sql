/*

    Lavet af Gruppe D
    Skole Projekt - Cupcake
    2. Semester
    3NF skulle gerne være opfyldt...

*/

/* Users */
CREATE TABLE users (
id SERIAL PRIMARY KEY,
email VARCHAR(255) UNIQUE NOT NULL,
password_hash TEXT NOT NULL, /* Safety */
role VARCHAR(20) NOT NULL DEFAULT 'customer', /* Customer, Admin, Support (?), Diddy */
username VARCHAR(100) NOT NULL,
created_at TIMESTAMP DEFAULT NOW() /* Auto */
);

/* Cupcake bottoms */
CREATE TABLE cupcake_flavor (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
price DECIMAL(10,2) NOT NULL
);

/* Cupcake toppings */
CREATE TABLE cupcake_toppings (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
price DECIMAL(10,2) NOT NULL
);

/* Orders */
CREATE TABLE orders (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id) ON DELETE CASCADE,
status VARCHAR(20) NOT NULL DEFAULT 'open', /* Open, Pending, Accepted, Refunded, Denied etc... */
created_at TIMESTAMP DEFAULT NOW() /* Auto */
);

/* Per order (order lines) */
CREATE TABLE order_per (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id) ON DELETE CASCADE,
bottom_id INT REFERENCES cupcake_flavor(id),
topping_id INT REFERENCES cupcake_toppings(id),
quantity INT NOT NULL DEFAULT 1
);

/* Transactions */
CREATE TABLE transactions (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id) ON DELETE CASCADE,
amount DECIMAL(10,2) NOT NULL,
created_at TIMESTAMP DEFAULT NOW(), /* Auto */
description TEXT
);