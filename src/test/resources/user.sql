-- Insert sample users
INSERT INTO user (name, profilePictureURL, username, email, password, address, phone_number, active)
VALUES
    ('John Doe', 'profile.jpg', 'admin', 'admin@gmail.com', '$2a$10$J7jecF/KkINXo/UNRtuI4urTc8r66hlheI465PycaTez/WAuOhNYu', '123 Main St', '123-456-7890', true),
    ('Jane Smith', 'profile.jpg', 'janesmith', 'jane@example.com', '$2a$10$zKT1KlCvXDKkXgAoqfgYm.CjsG0aA4bEDqf/8ZuhH5YZ9vFg0HUsa', '456 Elm St', '987-654-3210', true);

-- Assign roles to users
INSERT INTO user_roles (user_id, role)
VALUES
    (1, 'ADMIN'),
    (2, 'USER');
