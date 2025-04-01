--liquibase formatted sql

--changeset iorhael:1741471237245
CREATE TABLE products
(
    id              UUID PRIMARY KEY             DEFAULT gen_random_uuid(),
    vendor_id       UUID                NOT NULL REFERENCES vendors ON DELETE SET NULL,
    name            VARCHAR(255) UNIQUE NOT NULL,
    description     TEXT                NOT NULL,
    characteristics JSONB               NOT NULL,
    verified        BOOLEAN             NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ         NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ         NOT NULL DEFAULT now()
);

--rollback DROP TABLE products
