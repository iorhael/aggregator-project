--liquibase formatted sql

--changeset iorhael:1741467553459
CREATE TABLE retailers
(
    id          UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    owner_id    UUID        NOT NULL UNIQUE REFERENCES users ON DELETE SET NULL,
    name        VARCHAR(50) NOT NULL,
    description TEXT        NOT NULL,
    website     VARCHAR(255),
    email       VARCHAR(255),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

--rollback DROP TABLE retailers
