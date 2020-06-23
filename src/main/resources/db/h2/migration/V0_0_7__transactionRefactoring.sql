/*
    SQL Dialect : H2
    DB Version : 0.0.7
    Description : Replace column Total with column Commission that represents amount to bill on debtor user.
    Also add Bill table to save which transactions have been settled.
 */

ALTER TABLE TRANSACTION
    DROP COLUMN IF EXISTS TOTAL;

ALTER TABLE TRANSACTION
    ADD COLUMN IF NOT EXISTS COMMISSION DECIMAL(5, 2) NOT NULL default 0.0;

UPDATE TRANSACTION
SET TRANSACTION.COMMISSION = TRANSACTION.AMOUNT * 0.005
WHERE TRANSACTION.COMMISSION = 0.0;

CREATE TABLE BILL
(
    ID            IDENTITY                 NOT NULL,
    USER_ID       INTEGER                  NOT NULL,
    START_DATE    TIMESTAMP WITH TIME ZONE NOT NULL,
    END_DATE      TIMESTAMP WITH TIME ZONE NOT NULL,
    CREATION_DATE TIMESTAMP WITH TIME ZONE NOT NULL,
    TOTAL         DECIMAL(5, 2)            NOT NULL,
    CONSTRAINT Bill_pk PRIMARY KEY (id)
);

ALTER TABLE BILL
    ADD CONSTRAINT USER_Bill_fk
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;