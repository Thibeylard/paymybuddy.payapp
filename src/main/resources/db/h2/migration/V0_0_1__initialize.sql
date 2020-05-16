/*
    SQL Dialect : H2
    DB Version : 0.0.1
    Description : Initialize database for first deliverable
 */

CREATE TABLE User
(
    id       IDENTITY    NOT NULL,
    username VARCHAR(30) NOT NULL,
    mail     VARCHAR(50) NOT NULL,
    password VARCHAR(80) NOT NULL,
    CONSTRAINT user_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX mail_idx
    ON User
        (mail ASC);

CREATE UNIQUE INDEX username_idx
    ON User
        (username ASC);

CREATE TABLE User_Role
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT User_Role_pk PRIMARY KEY (user_id, role_id)
);

ALTER TABLE User_Role
    ADD CONSTRAINT USER_User_Role_fk
        FOREIGN KEY (user_id)
            REFERENCES User (id)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
