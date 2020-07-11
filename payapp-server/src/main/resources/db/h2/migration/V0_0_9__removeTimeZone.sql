/*
    SQL Dialect : H2
    DB Version : 0.0.9
    Description : Refactored tables with timestamp with time zone - Convert to Datetime
 */

ALTER TABLE TRANSACTION
    ALTER COLUMN DATE DATETIME;

ALTER TABLE BANK_OPERATION
    ALTER COLUMN DATE DATETIME;

ALTER TABLE BILL
    ALTER COLUMN CREATION_DATE DATETIME;
ALTER TABLE BILL
    ALTER COLUMN START_DATE DATETIME;
ALTER TABLE BILL
    ALTER COLUMN END_DATE DATETIME;