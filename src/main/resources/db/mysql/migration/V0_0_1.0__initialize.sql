/*
    SQL Dialect : MySQL
    DB Version : 0.0.1.0
    Description : Initialize database for production
 */

CREATE TABLE User
(
    id       INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(30)        NOT NULL,
    mail     VARCHAR(50)        NOT NULL,
    password VARCHAR(80)        NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX mail_idx
    ON User
        (mail ASC);

CREATE UNIQUE INDEX username_idx
    ON User
        (username ASC);

CREATE TABLE Bill
(
    id            INT AUTO_INCREMENT NOT NULL,
    user_id       INT                NOT NULL,
    creation_date DATETIME           NOT NULL,
    start_date    DATETIME           NOT NULL,
    end_date      DATETIME           NOT NULL,
    total         DECIMAL(5, 2)      NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE User_Role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id)
);


CREATE TABLE Transaction
(
    id          INT           NOT NULL,
    debtor_id   INT           NOT NULL,
    creditor_id INT           NOT NULL,
    description VARCHAR(40)   NOT NULL,
    amount      DECIMAL(5, 2) NOT NULL,
    commission  DECIMAL(4, 4) NOT NULL,
    date        DATETIME      NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE Bank_Account
(
    id             INT AUTO_INCREMENT NOT NULL,
    description    VARCHAR(40),
    IBAN           VARCHAR(35)        NOT NULL,
    owner_fullname VARCHAR(50)        NOT NULL,
    user_id        INT                NOT NULL,
    PRIMARY KEY (id)
);


CREATE UNIQUE INDEX iban_idx
    ON Bank_Account
        (IBAN);

CREATE TABLE Bank_Operation
(
    id              INT AUTO_INCREMENT NOT NULL,
    bank_account_id INT                NOT NULL,
    amount          DECIMAL(5, 2)      NOT NULL,
    date            DATETIME           NOT NULL,
    PRIMARY KEY (id)
);


CREATE TABLE Contact
(
    user_a_id INT NOT NULL,
    user_b_id INT NOT NULL,
    PRIMARY KEY (user_a_id, user_b_id)
);


ALTER TABLE Contact
    ADD CONSTRAINT user_connection_fk
        FOREIGN KEY (user_b_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Contact
    ADD CONSTRAINT user_connection_fk1
        FOREIGN KEY (user_a_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Bank_Account
    ADD CONSTRAINT user_bank_account_fk
        FOREIGN KEY (user_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Transaction
    ADD CONSTRAINT user_transaction_fk
        FOREIGN KEY (debtor_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Transaction
    ADD CONSTRAINT user_transaction_fk1
        FOREIGN KEY (creditor_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE User_Role
    ADD CONSTRAINT user_role_fk
        FOREIGN KEY (user_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Bill
    ADD CONSTRAINT user_bills_fk
        FOREIGN KEY (user_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Bank_Operation
    ADD CONSTRAINT bank_account_bank_operation_fk
        FOREIGN KEY (bank_account_id)
            REFERENCES Bank_Account (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;