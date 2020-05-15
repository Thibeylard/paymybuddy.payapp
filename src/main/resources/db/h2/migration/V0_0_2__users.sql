/*
    SQL Dialect : H2
    DB Version : 0.0.2
    Description : Insert users into database.
 */

INSERT INTO User (username, mail, password)
VALUES ('martin', 'martin@gmail.com', 'martinpass'),
       ('adrien', 'adrien@hotmail.com', 'adrienpass'),
       ('stephane', 'stephane@paymybuddy.com', 'stephanepass');

INSERT INTO User_Role (user_id, role_id)
VALUES ((SELECT id FROM USER WHERE username = 'martin'), 1),
       ((SELECT id FROM USER WHERE username = 'adrien'), 1),
       ((SELECT id FROM USER WHERE username = 'stephane'), 0);