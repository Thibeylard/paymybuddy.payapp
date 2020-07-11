/*
    SQL Dialect : H2
    DB Version : 0.0.6
    Description : Add BankAccount and BankOperation tables
 */

CREATE TABLE Bank_Account
(
    id             IDENTITY    NOT NULL,
    description    VARCHAR(40),
    IBAN           VARCHAR(35) NOT NULL,
    owner_fullname VARCHAR(50) NOT NULL,
    user_id        INTEGER     NOT NULL,
    CONSTRAINT bank_account_id PRIMARY KEY (id)
);


CREATE UNIQUE INDEX IBAN_idx
    ON Bank_Account
        (IBAN);

CREATE TABLE Bank_Operation
(
    id              IDENTITY                 NOT NULL,
    bank_account_id INTEGER                  NOT NULL,
    amount          DECIMAL(6, 2)            NOT NULL,
    date            TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT Bank_Operation_pk PRIMARY KEY (id)
);

ALTER TABLE Bank_Account
    ADD CONSTRAINT USER_Bank_Account_fk
        FOREIGN KEY (user_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Bank_Operation
    ADD CONSTRAINT Bank_Account_Bank_Operation_fk
        FOREIGN KEY (bank_account_id)
            REFERENCES Bank_Account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;