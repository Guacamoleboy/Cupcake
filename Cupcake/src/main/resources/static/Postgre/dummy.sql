/* Needed for Hash Generation Apparently */
CREATE EXTENSION IF NOT EXISTS pgcrypto;

/* Hashed Users */
INSERT INTO users (email, password_hash, role, username) VALUES
('admin@olskercupcakes.dk', crypt('admin', gen_salt('bf')), 'admin', 'Admin'),
('customer@olskercupcakes.dk', crypt('customer', gen_salt('bf')), 'customer', 'Customer'),
('jonas68@live.dk', crypt('1', gen_salt('bf')), 'customer', 'Guacamoleboy'),
('ebou@live.dk', crypt('Ebou', gen_salt('bf')), 'customer', 'Ebou'),
('rovelt123@live.dk', crypt('rovelt123', gen_salt('bf')), 'customer', 'Rovelt123');

/* Cupcake Flavor */
INSERT INTO cupcake_flavor (name, price) VALUES
('Chokolade', 15.00),
('Vanilje', 12.50),
('Halloween', 17.00),
('Red Velvet', 16.00),
('Double Chocolate', 19.00);

/* Cupcake Toppings */
INSERT INTO cupcake_toppings (name, price) VALUES
('Jordbær', 7.50),
('Chokoladeglasur', 8.00),
('Flødeskum', 5.00),
('Nossehår', 20.00), /* Det vokser langsomt bby girl.. Derfor prisen jooooooooo */
('Vaniljecreme', 12.00);

/* Orders (Total) */
INSERT INTO orders (user_id, status) VALUES
(2, 'open');

/* Per Order */
INSERT INTO order_per (order_id, bottom_id, topping_id, quantity) VALUES
(1, 1, 1, 2),
(1, 2, 2, 1);

/*
INSERT INTO transactions (user_id, amount, description) VALUES
(2, 100.00, 'Refund'); -- Refund

INSERT INTO transactions (user_id, amount, description) VALUES
(2, -48.00, 'Payment for your order (#591915915)'); -- Payment
*/