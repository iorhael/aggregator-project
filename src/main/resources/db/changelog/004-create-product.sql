--liquibase formatted sql

--changeset iorhael:1741471237245
CREATE TABLE products
(
    id            UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    parent_id     UUID         REFERENCES products ON DELETE SET NULL,
    name          VARCHAR(255) NOT NULL,
    description   TEXT         NOT NULL,
    creation_type VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT now()
);

--rollback DROP TABLE products
