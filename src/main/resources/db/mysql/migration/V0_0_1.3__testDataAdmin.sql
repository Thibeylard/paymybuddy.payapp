/*
    SQL Dialect : MySQL
    DB Version : 0.0.1.3
    Description : Added admin user for admin pages
 */

INSERT INTO User (id, username, mail, password)
VALUES (99, 'tibobey', 'thibaut.beylard@example.com',
        '$2y$10$xM3F8IX/QqYdh41VoA79OeTvz8IbhF0PoH.NRSIWuN4XNFLb1osGy'); -- adminpass

INSERT INTO User_Role (user_id, role_id)
VALUES (99, 0);
