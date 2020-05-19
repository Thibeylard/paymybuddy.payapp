/*
    SQL Dialect : H2
    DB Version : 0.0.2
    Description : Insert users into database.
 */

INSERT INTO User (username, mail, password)
VALUES ('martin', 'martin@gmail.com', '$2y$10$uGQEMhHXORXkkZZH57aX6OttU7D9Oy/5SPmOCEky5XQv1eJ.zS30O'),   // martinpass
       ('adrien', 'adrien@hotmail.com', '$2y$10$taZoKdA8hz0n/xfcgmqfPetcazz5xUwFSUgSmyYAYiA8bx/TIrtIW'), // adrienpass
       ('stephane', 'stephane@paymybuddy.com',
        '$2y$10$nvJ0R.vN3ZZI6PEcg5IMnelTjb1T7ZAWBFPoOKf9HT8MSKH2NvMiW'); // stephanepass

INSERT INTO User_Role (user_id, role_id)
VALUES ((SELECT id FROM USER WHERE username = 'martin'), 1),
       ((SELECT id FROM USER WHERE username = 'adrien'), 1),
       ((SELECT id FROM USER WHERE username = 'stephane'), 0);