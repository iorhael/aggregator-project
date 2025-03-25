--liquibase formatted sql

--changeset iorhael:1741472548609
CREATE TABLE categories
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    name        VARCHAR(30) NOT NULL UNIQUE,
    description TEXT,
    parent_id   UUID        REFERENCES categories ON DELETE SET NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

--rollback DROP TABLE categories
