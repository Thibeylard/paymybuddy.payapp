/*
    SQL Dialect : MySQL
    DB Version : 0.0.1.1
    Description : Insert data for production manual tests
 */

INSERT INTO User (id, username, mail, password)
VALUES (1, 'nharvey', 'nelson.harvey@example.com',
        '$2y$10$zw4WuLfB4ZGq4U//kb9q6uZpZUr7dtDDHt7A1Vi5iWW4o24fnTFZK'), // tinkerbe459
       (2, 'leslin', 'leslie.austin@example.com',
        '$2y$10$zm1UAsm0gsv1scOZ1gS5a.aSSyMPkigy6igOEZkWYUuGAs7vj.ch2'), // superarcher
       (3, 'antowright', 'antonio.wright@example.com',
        '$2y$10$u1A7FWaS6NS9QKhYF/fWKOdrW1aXgxE173qzmPyRC0.xlmcSkTiGC'), // book7books
       (4, 'dianoreno', 'diana.moreno@example.com',
        '$2y$10$9Eofm3/Do5.4XSHMBAJ.OeiLoRYBXZVwLQU2MDB0VPUEDk3BGTDJK'); // bulletgun

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

INSERT INTO Transaction (ID, DEBTOR_ID, CREDITOR_ID, DESCRIPTION, AMOUNT, COMMISSION, DATE)
VALUES (1, 1, 2, 'restaurant bill', 29.00, 0.145, '2020-01-01 12:22:37+01'),
       (2, 3, 1, 'cinema ticket', 11.00, 0.055, '2020-02-04 21:36:02+01'),
       (3, 2, 3, 'misc drinks', 8.90, 0.0445, '2020-02-25 17:04:10+01'),
       (4, 3, 4, 'car pool', 10.00, 0.05, '2020-04-09 14:58:16+02'),
       (5, 1, 4, 'tuesday lunch', 23.50, 0.1175, '2020-06-18 08:23:31+02'),
       (6, 3, 2, 'flat-share week rent', 40.00, 0.2, '2020-07-08 19:53:54+02');

INSERT INTO Bank_Account (ID, USER_ID, OWNER_FULLNAME, DESCRIPTION, IBAN)
VALUES (1, 1, 'Nelson HARVEY', 'My bank account', 'GB90CMXF03231681002625'),
       (2, 2, 'Leslie AUSTIN', 'Main bank account', 'GB89IJNZ65882840666637'),
       (3, 3, 'Antonio WRIGHT', 'HSBC Holdings', 'GB94IKPH80981735146572'),
       (4, 4, 'Diana MORENO', 'Barclays account', 'GB54DALF15831003745338');

INSERT INTO Bank_Operation (ID, BANK_ACCOUNT_ID, DATE, AMOUNT)
VALUES (1, 1, '2020-01-01 00:00:00+01', 100),
       (2, 2, '2020-01-01 00:00:00+01', 100),
       (3, 3, '2020-01-01 00:00:00+01', 100),
       (4, 4, '2020-01-01 00:00:00+01', 100),
       (5, 1, '2020-02-05 09:40:32+01', -10),
       (6, 3, '2020-04-06 16:15:36+02', 20),
       (7, 1, '2020-06-20 13:49:27+02', 30),
       (8, 2, '2020-07-10 15:34:53+02', -60);

INSERT INTO Bill (ID, USER_ID, START_DATE, END_DATE, CREATION_DATE, TOTAL)
VALUES (1, 1, '2020-01-01 00:00:00+01', '2020-01-31 23:59:59+01', '2020-02-01 00:00:00+01', 0.145),
       (2, 3, '2020-01-01 00:00:00+01', '2020-06-30 23:59:59+02', '2020-07-01 00:00:00+02', 0.1);