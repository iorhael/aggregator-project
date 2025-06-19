--liquibase formatted sql

--changeset iorhael:1743451422241
CREATE TABLE vendors
(
    id          UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    name        VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);

--rollback DROP TABLE vendors
