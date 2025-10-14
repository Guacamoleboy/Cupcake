/*

    Lavet af Gruppe D
    Skole Projekt - Cupcake
    2. Semester
    3NF

    Sidst opdateret af: Guacamoleboy
    Dato: 14/10-2025

*/

/* Users */
CREATE TABLE users (
id SERIAL PRIMARY KEY,
email VARCHAR(255) UNIQUE NOT NULL,
password_hash TEXT NOT NULL,
role VARCHAR(20) NOT NULL DEFAULT 'customer',
username VARCHAR(100) UNIQUE NOT NULL,
phone VARCHAR(20) UNIQUE,
payment_attached BOOLEAN DEFAULT FALSE,
created_at TIMESTAMP DEFAULT NOW()
);

/* Payment info */
CREATE TABLE payment_info (
id SERIAL PRIMARY KEY,
user_id INTEGER NOT NULL REFERENCES users(id) ON DELETE CASCADE,
card_number VARCHAR(19) NOT NULL,
cardholder_name VARCHAR(255) NOT NULL,
expiry_month INTEGER NOT NULL CHECK (expiry_month BETWEEN 1 AND 12),
expiry_year INTEGER NOT NULL CHECK (expiry_year BETWEEN 2025 AND 2100),
cvv_hash TEXT NOT NULL,
created_at TIMESTAMP DEFAULT NOW()
);

/* Cupcake bottoms */
CREATE TABLE cupcake_flavor (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
price DECIMAL(10,2) NOT NULL
);

/* For order.html */
CREATE TABLE categories (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL UNIQUE
);

/* Products */
CREATE TABLE products (
id SERIAL PRIMARY KEY,
name VARCHAR(150) NOT NULL,
description TEXT,
price DECIMAL(10,2) NOT NULL,
image_url TEXT,
category_id INT REFERENCES categories(id) ON DELETE SET NULL,
topping_id INT REFERENCES cupcake_toppings(id) ON DELETE SET NULL,
flavor_id INT REFERENCES cupcake_flavor(id) ON DELETE SET NULL,
created_at TIMESTAMP DEFAULT NOW()
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
CREATE TABLE order_items (
id SERIAL PRIMARY KEY,
order_id INT REFERENCES orders(id) ON DELETE CASCADE,
product_id INT REFERENCES products(id),
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