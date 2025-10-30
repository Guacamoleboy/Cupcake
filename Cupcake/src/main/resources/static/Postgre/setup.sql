/*

    Lavet af Gruppe D
    Skole Projekt - Cupcake
    2. Semester
    Skulle gerne følge 3NF

    Sidst opdateret af: Guacamoleboy
    Dato: 30/10-2025

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
balance DECIMAL(12, 2) DEFAULT 0.00,
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

/* Cupcake toppings */
CREATE TABLE cupcake_toppings (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
price DECIMAL(10,2) NOT NULL
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

/* Delivery Method */
CREATE TABLE delivery_methods (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE,
price DECIMAL(10,2) NOT NULL DEFAULT 0.0
);

/* Payment Method */
CREATE TABLE payment_methods (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE
);

/* Orders */
CREATE TABLE orders (
id SERIAL PRIMARY KEY,
user_id INT REFERENCES users(id) ON DELETE CASCADE,
status VARCHAR(20) NOT NULL DEFAULT 'open',
delivery_method_id INT REFERENCES delivery_methods(id) ON DELETE SET NULL,
payment_method_id INT REFERENCES payment_methods(id) ON DELETE SET NULL,
delivery_address TEXT,
created_at TIMESTAMP DEFAULT NOW()
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

/* Coupons */
CREATE TABLE coupons (
id SERIAL PRIMARY KEY,
code VARCHAR(50) UNIQUE NOT NULL,
discount_percent INT NOT NULL CHECK (discount_percent BETWEEN 1 AND 100),
valid_from TIMESTAMP DEFAULT NOW(),
valid_until TIMESTAMP
);

/* Refunds */
CREATE TABLE refunds (
id SERIAL PRIMARY KEY,
order_id INT NOT NULL,
user_id INT NOT NULL,
reason VARCHAR(255) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
status VARCHAR(50) NOT NULL DEFAULT 'active',
FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);