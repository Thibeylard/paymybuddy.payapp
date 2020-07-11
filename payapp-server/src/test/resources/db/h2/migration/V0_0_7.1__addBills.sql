/*
    SQL Dialect : H2
    DB Version : 0.0.7.1
    Description : Populate database with test data for Bill table
 */

INSERT INTO BILL (ID, USER_ID, START_DATE, END_DATE, CREATION_DATE, TOTAL)
VALUES (1, 1, '2019-11-01 00:00:00+01', '2019-11-30 23:59:59+01', '2019-12-01 00:00:00+01', 0.25);

