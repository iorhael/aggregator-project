--liquibase formatted sql

--changeset iorhael:1741471404126
CREATE TABLE product_cards
(
    id                    UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    product_id            UUID        NOT NULL REFERENCES products,
    retailer_id           UUID        NOT NULL REFERENCES retailers,
    additional_properties JSONB,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at            TIMESTAMPTZ NOT NULL DEFAULT now()
);

--rollback DROP TABLE product_cards
