--liquibase formatted sql

--changeset iorhael:1741347591209
CREATE TABLE users
(
    id       UUID PRIMARY KEY,
    username VARCHAR(30)  NOT NULL UNIQUE,
    email    VARCHAR(255) NOT NULL UNIQUE,
    role     VARCHAR(30)  NOT NULL
);

--rollback DROP TABLE users
