/*
    SQL Dialect : H2
    DB Version : 0.0.3
    Description : Added tables needed to handle money transactions between users
 */

CREATE TABLE Transaction
(
    id          INTEGER       NOT NULL,
    debtor_id   INTEGER       NOT NULL,
    creditor_id INTEGER       NOT NULL,
    description VARCHAR(40)   NOT NULL,
    amount      DECIMAL(5, 2) NOT NULL,
    date        DATE          NOT NULL,
    CONSTRAINT Transaction_pk PRIMARY KEY (id)
);


CREATE TABLE Contact
(
    user_a_id INTEGER NOT NULL,
    user_b_id INTEGER NOT NULL,
    CONSTRAINT Contact_pk PRIMARY KEY (user_a_id, user_b_id)
);


ALTER TABLE Contact
    ADD CONSTRAINT USER_Connection_fk
        FOREIGN KEY (user_b_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Contact
    ADD CONSTRAINT USER_Connection_fk1
        FOREIGN KEY (user_a_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Transaction
    ADD CONSTRAINT USER_Transaction_fk
        FOREIGN KEY (debtor_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE Transaction
    ADD CONSTRAINT USER_Transaction_fk1
        FOREIGN KEY (creditor_id)
            REFERENCES USER (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;