# Ecommerce

#Sample Data 
##insert to database

INSERT INTO category (name) VALUES ('Electronics');
INSERT INTO category (name) VALUES ('Fashion');
INSERT INTO category (name) VALUES ('Home & Garden');
INSERT INTO category (name) VALUES ('Sports & Outdoors');
INSERT INTO category (name) VALUES ('Toys & Games');

INSERT INTO product (price, category_id, description, name, stock) VALUES (25.99, 1, 'High-quality headphones', 'Headphones A', 100);
INSERT INTO product (price, category_id, description, name, stock) VALUES (19.99, 2, 'Stylish T-shirt', 'T-shirt B', 20);
INSERT INTO product (price, category_id, description, name, stock) VALUES (34.50, 3, 'Soft and cozy blanket', 'Blanket C', 40);
INSERT INTO product (price, category_id, description, name, stock) VALUES (10.00, 1, 'Basic wired mouse', 'Mouse D', 60);
INSERT INTO product (price, category_id, description, name, stock) VALUES (49.99, 4, 'Durable hiking backpack', 'Backpack E', 35);
INSERT INTO product (price, category_id, description, name, stock) VALUES (7.99, 2, 'Colorful socks pack', 'Socks F', 90);
INSERT INTO product (price, category_id, description, name, stock) VALUES (149.99, 5, 'Interactive toy robot', 'Robot G', 120);
INSERT INTO product (price, category_id, description, name, stock) VALUES (39.95, 3, 'Elegant table lamp', 'Lamp H', 9);
INSERT INTO product (price, category_id, description, name, stock) VALUES (5.50, 2, 'Pack of colorful pens', 'Pens I', 1);
INSERT INTO product (price, category_id, description, name, stock) VALUES (69.99, 4, 'Wireless gaming mouse', 'Mouse J', 0);

INSERT INTO review (rating, product_id, content) VALUES (4, 1, 'Great product, highly recommended.');
INSERT INTO review (rating, product_id, content) VALUES (5, 2, 'Best purchase ever!');
INSERT INTO review (rating, product_id, content) VALUES (3, 3, 'It is okay, not bad but not outstanding either.');
INSERT INTO review (rating, product_id, content) VALUES (2, 4, 'Disappointed with the quality.');
INSERT INTO review (rating, product_id, content) VALUES (5, 5, 'Absolutely love it!');
INSERT INTO review (rating, product_id, content) VALUES (4, 6, 'Impressed with the features.');
INSERT INTO review (rating, product_id, content) VALUES (3, 7, 'Average product, nothing special.');
INSERT INTO review (rating, product_id, content) VALUES (5, 8, 'Worth every penny!');
INSERT INTO review (rating, product_id, content) VALUES (2, 9, 'Not as described.');
INSERT INTO review (rating, product_id, content) VALUES (4, 10, 'Good value for money.');
INSERT INTO review (rating, product_id, content) VALUES (3, 1, 'Decent product, could be better.');
INSERT INTO review (rating, product_id, content) VALUES (5, 2, 'Exceeded my expectations!');
INSERT INTO review (rating, product_id, content) VALUES (4, 3, 'Satisfied with the purchase.');
INSERT INTO review (rating, product_id, content) VALUES (2, 4, 'Not worth the price.');
INSERT INTO review (rating, product_id, content) VALUES (3, 5, 'Just okay, not outstanding.');
INSERT INTO review (rating, product_id, content) VALUES (5, 6, 'Highly impressed with the quality.');
INSERT INTO review (rating, product_id, content) VALUES (4, 7, 'Good product, serves its purpose.');
INSERT INTO review (rating, product_id, content) VALUES (3, 8, 'Average, nothing special.');
INSERT INTO review (rating, product_id, content) VALUES (5, 9, 'Thrilled with my purchase!');
INSERT INTO review (rating, product_id, content) VALUES (2, 10, 'Regret buying it.');
