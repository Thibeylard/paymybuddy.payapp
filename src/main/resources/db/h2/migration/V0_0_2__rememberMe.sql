/*
    SQL Dialect : H2
    DB Version : 0.0.2
    Description : Add specific table schema for Spring remember me service
 */

CREATE TABLE persistent_logins
(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
);