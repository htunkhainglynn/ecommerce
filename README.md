# Ecommerce

# SampleData 

INSERT INTO ecommerce.category (name) VALUES ('Electronics');
INSERT INTO ecommerce.category (name) VALUES ('Fashion');
INSERT INTO ecommerce.category (name) VALUES ('Home & Garden');
INSERT INTO ecommerce.category (name) VALUES ('Sports & Outdoors');
INSERT INTO ecommerce.category (name) VALUES ('Toys & Games');

INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (25.99, 1, 'High-quality headphones', 'Headphones A', 100);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (19.99, 2, 'Stylish T-shirt', 'T-shirt B', 20);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (34.50, 3, 'Soft and cozy blanket', 'Blanket C', 40);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (10.00, 1, 'Basic wired mouse', 'Mouse D', 60);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (49.99, 4, 'Durable hiking backpack', 'Backpack E', 35);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (7.99, 2, 'Colorful socks pack', 'Socks F', 90);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (149.99, 5, 'Interactive toy robot', 'Robot G', 120);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (39.95, 3, 'Elegant table lamp', 'Lamp H', 9);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (5.50, 2, 'Pack of colorful pens', 'Pens I', 1);
INSERT INTO ecommerce.product (price, category_id, description, name, stock) VALUES (69.99, 4, 'Wireless gaming mouse', 'Mouse J', 0);

INSERT INTO ecommerce.review (rating, product_id, content) VALUES (4, 1, 'Great product, highly recommended.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 2, 'Best purchase ever!');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (3, 3, 'It is okay, not bad but not outstanding either.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (2, 4, 'Disappointed with the quality.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 5, 'Absolutely love it!');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (4, 6, 'Impressed with the features.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (3, 7, 'Average product, nothing special.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 8, 'Worth every penny!');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (2, 9, 'Not as described.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (4, 10, 'Good value for money.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (3, 1, 'Decent product, could be better.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 2, 'Exceeded my expectations!');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (4, 3, 'Satisfied with the purchase.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (2, 4, 'Not worth the price.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (3, 5, 'Just okay, not outstanding.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 6, 'Highly impressed with the quality.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (4, 7, 'Good product, serves its purpose.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (3, 8, 'Average, nothing special.');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (5, 9, 'Thrilled with my purchase!');
INSERT INTO ecommerce.review (rating, product_id, content) VALUES (2, 10, 'Regret buying it.');
