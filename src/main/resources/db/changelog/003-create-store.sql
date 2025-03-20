--liquibase formatted sql

--changeset iorhael:1741469232943
CREATE TABLE stores
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    retailer_id   UUID         NOT NULL REFERENCES retailers,
    address       VARCHAR(255) NOT NULL,
    phone         VARCHAR(15),
    opening_hours JSONB
);

--rollback DROP TABLE stores
