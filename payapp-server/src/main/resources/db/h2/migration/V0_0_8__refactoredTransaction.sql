/*
    SQL Dialect : H2
    DB Version : 0.0.8
    Description : Refactored Transaction table column Zoned_date_time. Shorter name Data that match SQL equivalent
                and set it as not null.
 */

ALTER TABLE TRANSACTION
    ADD COLUMN IF NOT EXISTS DATE TIMESTAMP WITH TIME ZONE NOT NULL default '2000-01-01 00:00:00+00';

UPDATE TRANSACTION
SET TRANSACTION.DATE = TRANSACTION.ZONED_DATE_TIME
WHERE TRANSACTION.DATE = '2000-01-01 00:00:00+00';

ALTER TABLE TRANSACTION
    DROP COLUMN IF EXISTS ZONED_DATE_TIME;
