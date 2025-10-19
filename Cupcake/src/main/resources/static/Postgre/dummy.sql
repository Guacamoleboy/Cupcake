/* Needed for Hash Generation Apparently */
CREATE EXTENSION IF NOT EXISTS pgcrypto;

/* Hashed Users */
INSERT INTO users (email, password_hash, role, username) VALUES
('admin@olskercupcakes.dk', crypt('admin', gen_salt('bf')), 'admin', 'Admin'),
('customer@olskercupcakes.dk', crypt('customer', gen_salt('bf')), 'customer', 'Customer'),
('jonas68@live.dk', crypt('1', gen_salt('bf')), 'customer', 'Guacamoleboy'),
('ebou@live.dk', crypt('Ebou', gen_salt('bf')), 'customer', 'Ebou'),
('rovelt123@live.dk', crypt('rovelt123', gen_salt('bf')), 'customer', 'Rovelt123');

/* Cupcake Flavor (bunde) */
INSERT INTO cupcake_flavor (name, price) VALUES
('Chokolade', 15.00),
('Vanilje', 12.50),
('Red Velvet', 16.00),
('Sandkage', 13.50);

/* Cupcake Toppings */
INSERT INTO cupcake_toppings (name, price) VALUES
('Jordbær', 6.00),
('Chokolade', 6.50),
('Hindbær', 6.00),
('Flødeskum', 5.00),
('Marshmallow Sprinkle', 6.50),
('Skum', 5.00),
('Marshmallow Fluff', 6.50),
('Is', 7.00),
('Vaniljeskum', 5.50),
('Marengs', 6.00);

/* Categories */
INSERT INTO categories (name) VALUES
('Chokolade'),
('Frugt'),
('Creme');

/* Products (flavor_id = bund) */
INSERT INTO products (name, description, price, image_url, category_id, topping_id, flavor_id) VALUES
('Det røde bær', 'Red velvet bund med frisk jordbærtop.', 29.95, 'images/products/cupcake-1.png', 2, 1, 3),
('Cherry On Top', 'Blød vaniljebund toppet med chokoladecreme.', 27.95, 'images/products/cupcake-2.png', 2, 2, 2),
('Hindbærdrøm', 'Vaniljebund med sød hindbærtopping.', 27.95, 'images/products/cupcake-3.png', 2, 3, 2),
('Skumtop', 'Rig chokoladebund toppet med let flødeskum.', 28.95, 'images/products/cupcake-4.png', 3, 4, 1),
('Sweet Delight', 'Vanilje med marshmallow og farverige sprinkles.', 28.95, 'images/products/cupcake-5.png', 3, 5, 2),
('Skykys', 'Luftig vanilje med luftig skumtopping.', 27.95, 'images/products/cupcake-6.png', 3, 6, 2),
('Sommerdrøm', 'Sandkagebund med frisk skum – en sommerfavorit.', 27.95, 'images/products/cupcake-7.png', 2, 6, 4),
('Julehjerte', 'Chokolade og skum i julet harmoni.', 28.95, 'images/products/cupcake-8.png', 1, 6, 1),
('Midnatsskum', 'Dyb chokolade med mørk skumtop.', 28.95, 'images/products/cupcake-9.png', 1, 6, 1),
('Double Trouble', 'Ren chokolade i to lag – for ægte chokoladeelskere.', 29.95, 'images/products/cupcake-10.png', 1, 2, 1),
('Vanilla Cloud', 'Lys vanilje med let flødeskum.', 27.95, 'images/products/cupcake-11.png', 3, 4, 2),
('Velvet Kiss', 'Red velvet og skum i blød harmoni.', 28.95, 'images/products/cupcake-12.png', 2, 6, 3),
('Bærlyst', 'Chokolade med bær og flødeskum – frisk og intens.', 29.95, 'images/products/cupcake-13.png', 2, 1, 1),
('Kakao Klassik (Jul)', 'Ren chokoladekage til juletid.', 26.95, 'images/products/cupcake-14.png', 1, 2, 1),
('Frosty Dream', 'Vanilje med iskold topping – perfekt til sommer.', 29.95, 'images/products/cupcake-15.png', 3, 8, 2),
('Fluffy Cocoa', 'Chokoladebund med marshmallow fluff.', 28.95, 'images/products/cupcake-16.png', 1, 7, 1),
('Choco Creamy', 'Chokolade og vanilje i cremet balance.', 28.95, 'images/products/cupcake-17.png', 1, 2, 2),
('Snefnug', 'Julens vaniljekage med let skum.', 28.95, 'images/products/cupcake-18.png', 3, 6, 2),
('Crispy Cocoa', 'Chokoladebund toppet med sprød marengs.', 28.95, 'images/products/cupcake-19.png', 1, 10, 1);

/* Orders (Total) */
INSERT INTO orders (user_id, status) VALUES
(2, 'open'),
(3, 'open');

/* Per Order (Pre-defined) */
INSERT INTO order_items (order_id, product_id, quantity)
VALUES (2, 1, 3);

/* Per Order (Custom Cupcake) */
INSERT INTO order_items (order_id, bottom_id, topping_id, quantity) VALUES
(1, 1, 1, 2),
(1, 2, 2, 1);

/* test cupon code */
INSERT INTO coupons (code, discount_percent, valid_from, valid_until) VALUES (
'10procent',
10,
NOW(),
NOW() + INTERVAL '1 month'
);

/* Delivery methods */
INSERT INTO delivery_methods (name, price) VALUES
('GLS', 35.00),
('PostNord', 45.00),
('DAO', 30.00),
('Bring', 40.00),
('Afhent i butik', 0.00);

/* Payment methods */
INSERT INTO payment_methods (name) VALUES
('MobilePay'),
('VISA'),
('MasterCard'),
('Apple Pay');

/*
INSERT INTO transactions (user_id, amount, description) VALUES
(2, 100.00, 'Refund'); -- Refund

INSERT INTO transactions (user_id, amount, description) VALUES
(2, -48.00, 'Payment for your order (#591915915)'); -- Payment
*/

TRUNCATE TABLE products RESTART IDENTITY CASCADE;