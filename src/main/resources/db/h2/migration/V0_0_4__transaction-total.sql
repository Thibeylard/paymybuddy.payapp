/*
    SQL Dialect : H2
    DB Version : 0.0.4
    Description : Fix on Transaction table : forgot total column
 */

ALTER TABLE TRANSACTION
    ADD COLUMN TOTAL DECIMAL(5, 2) NOT NULL default 0.0;

UPDATE TRANSACTION
SET TRANSACTION.TOTAL = TRANSACTION.AMOUNT * 0.95
WHERE TRANSACTION.TOTAL = 0.0;