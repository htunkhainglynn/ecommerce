INSERT INTO orders (order_date, total_price, address_id, user_id, status)
VALUES
    ('2023-09-13', 99.99, 1, 1, 'SHIPPED'),
    ('2023-09-14', 49.99, 2, 2, 'PENDING'),
    ('2023-09-15', 129.99, 3, 3, 'RECEIVED');

INSERT INTO order_item (product_variant_id, quantity, order_id)
VALUES
    (1, 2, 1),
    (2, 1, 1),
    (3, 3, 2),
    (1, 1, 3);