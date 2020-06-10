/*
    SQL Dialect : H2
    DB Version : 0.0.5
    Description : Fix Transaction table
 */

CREATE SEQUENCE IF NOT EXISTS transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 5;

ALTER TABLE TRANSACTION
    DROP COLUMN IF EXISTS ID;

ALTER TABLE TRANSACTION
    ADD COLUMN IF NOT EXISTS ID IDENTITY NOT NULL default transaction_id_seq.nextval;

ALTER TABLE TRANSACTION
    ADD COLUMN IF NOT EXISTS ZONED_DATE_TIME TIMESTAMP WITH TIME ZONE;

ALTER TABLE TRANSACTION
    DROP COLUMN IF EXISTS DATE;
