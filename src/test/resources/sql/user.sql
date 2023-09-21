INSERT INTO user (active, email, name, password, phone_number, profile_pictureurl, username)
VALUES
    (1, 'admin@example.com', 'Admin', '$2a$10$UOAOIz2SPM1zGUR4vheiOOYwdw.gCdAHF.8TD7RcfmjEnTshzyEWe', '123-456-7890', 'profile1.jpg', 'admin'),
    (0, 'user@example.com', 'User One', '$2a$10$wCHYDZs0hOO075f1PubZ6.R/OkeFMRUDCTGOV9.1.htbQrbBxrR5e', '987-654-3210', 'profile2.jpg', 'user1'),
    (1, 'user2@example.com', 'User Two', '$2a$10$PdEgP4/IaRFzaxilu2Le/.KrAq5BkyW1pnG3XVNa8LDoZANOZBSsi', NULL, NULL, 'user2'),
    (1, 'user3@example.com', 'User Three', '$2a$10$wCHYDZs0hOO075f1PubZ6.R/OkeFMRUDCTGOV9.1.htbQrbBxrR5e', '987-654-3212', 'profile3.jpg', 'user3');

INSERT INTO role (user_id, role)
VALUES
    (1, 'ADMIN'),
    (2, 'USER'),
    (3, 'USER'),
    (4, 'USER');
