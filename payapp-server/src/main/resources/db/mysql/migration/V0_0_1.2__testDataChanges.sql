/*
    SQL Dialect : MySQL
    DB Version : 0.0.1.2
    Description : Minor changes for integration test with MySQL test data
 */

UPDATE Transaction
SET DATE = '2020-06-27 19:53:54.000000'
WHERE id = 6;

DELETE
FROM Bill
WHERE user_id = 3;

INSERT INTO Bill (ID, USER_ID, START_DATE, END_DATE, CREATION_DATE, TOTAL)
VALUES (2, 2, '2020-02-01 00:00:00.000000', '2020-02-28 23:59:59.000000', '2020-03-01 00:00:00.000000', 0.05);