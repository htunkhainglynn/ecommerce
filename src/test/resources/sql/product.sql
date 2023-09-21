INSERT INTO organization (category, vendor)
VALUES
    ('Technology', 'Tech Corp'),
    ('Finance', 'Finance Inc'),
    ('Retail', 'Retail Co');

INSERT INTO product (available, organization_id, sku, weight, description, name)
VALUES
    (1, 1, 1001, 2.5, 'Product 1 Description', 'Product 1 Name'),
    (1, 2, 1002, 1.8, 'Product 2 Description', 'Product 2 Name'),
    (0, 1, 1003, 3.2, 'Product 3 Description', 'Product 3 Name');

INSERT INTO product_variant (created_at, in_stock, price, purchase_price, quantity, updated_at, product_id, color, image_url, size)
VALUES
    ('2023-09-13', 1, 29.99, 19.99, 100, '2023-09-13', 1, 'Red', 'red.jpg', 'Medium'),
    ('2023-09-14', 0, 39.99, 29.99, 0, null, 1, 'Blue', 'blue.jpg', 'Large'),
    ('2023-09-15', 1, 19.99, 14.99, 75, '2023-09-15', 2, 'Black', 'black.jpg', 'Small'),
    ('2023-09-16', 1, 29.99, 19.99, 100, '2023-09-16', 2, 'White', 'white.jpg', 'Medium'),
    ('2023-09-17', 1, 39.99, 29.99, 50, '2023-09-17', 3, 'Green', 'green.jpg', 'Large'),
    ('2023-09-18', 1, 19.99, 14.99, 75, '2023-09-18', 3, 'Yellow', 'yellow.jpg', 'Small');

INSERT INTO expense (purchase_price, quantity, total, created_at, product_variant_id)
VALUES
    (19.99, 100, 1999.00, '2023-09-13', 1),
    (29.99, 75, 2249.25, '2023-09-15', 2),
    (19.99, 100, 1999.00, '2023-09-16', 1),
    (39.99, 50, 1999.50, '2023-09-17', 3);

