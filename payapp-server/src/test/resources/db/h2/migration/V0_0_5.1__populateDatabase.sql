/*
    SQL Dialect : H2
    DB Version : 0.0.5.1
    Description : Populate database with test data
 */

INSERT INTO User (id, username, mail, password)
VALUES (1, 'user1', 'user1@mail.com', 'user1pass'),
       (2, 'user2', 'user2@mail.com', 'user2pass'),
       (3, 'user3', 'user3@mail.com', 'user3pass'),
       (4, 'user4', 'user4@mail.com', 'user4pass');

INSERT INTO User_Role (user_id, role_id)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1);

INSERT INTO Contact (user_a_id, user_b_id)
VALUES (1, 2),
       (1, 3),
       (2, 3),
       (4, 1);

INSERT INTO Transaction (ID, DEBTOR_ID, CREDITOR_ID, DESCRIPTION, AMOUNT, TOTAL, ZONED_DATE_TIME)
VALUES (1, 1, 2, 'transaction1', 10.00, 9.50, '2020-01-01 00:00:00+01'),
       (2, 3, 1, 'transaction2', 10.00, 9.50, '2020-01-02 00:00:00+01'),
       (3, 2, 3, 'transaction3', 10.00, 9.50, '2020-01-03 00:00:00+01'),
       (4, 3, 4, 'transaction4', 10.00, 9.50, '2020-01-04 00:00:00+01'),
       (5, 1, 4, 'transaction5', 10.00, 9.50, '2020-01-05 00:00:00+01'),
       (6, 3, 2, 'transaction6', 10.00, 9.50, '2020-01-06 00:00:00+01')