/*
    SQL Dialect : H2
    DB Version : 0.0.6.1
    Description : Populate database with test data for Bank_Account and Bank_Operation tables
 */

INSERT INTO BANK_ACCOUNT (ID, USER_ID, OWNER_FULLNAME, DESCRIPTION, IBAN)
VALUES (1, 1, 'user1 NAME', 'main bank account', 'FR784356918887DW0BS628ARY69'),
       (2, 1, 'user1 NAME', 'second bank account', 'FR58664306261240R1H3658AT90'),
       (3, 2, 'user2 NAME', 'my bank account', 'FR975205942120TXG3F6799TZ87'),
       (4, 3, 'user3 NAME', 'my account from this bank', 'FR6300846780983EVO96L558099'),
       (5, 4, 'user4 NAME', 'my main bank account', 'FR3522190814281GZ6G972RO306');

INSERT INTO BANK_OPERATION (ID, BANK_ACCOUNT_ID, DATE, AMOUNT)
VALUES (1, 1, '2020-01-05 00:00:00+01', -20),
       (2, 2, '2020-02-01 00:00:00+01', 40),
       (3, 3, '2020-04-09 00:00:00+02', 30),
       (4, 4, '2020-06-02 00:00:00+02', 100),
       (5, 4, '2020-09-24 00:00:00+02', 75),
       (6, 3, '2020-10-17 00:00:00+02', -50),
       (7, 5, '2020-10-19 00:00:00+02', 30),
       (8, 5, '2020-11-23 00:00:00+02', 50);

