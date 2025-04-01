--liquibase formatted sql

--changeset iorhael:1741471404126
CREATE TABLE product_cards
(
    id                 UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    product_id         UUID        NOT NULL REFERENCES products,
    retailer_id        UUID        NOT NULL REFERENCES retailers,
    description        TEXT,
    warranty           SMALLINT,
    installment_period SMALLINT,
    max_delivery_time  SMALLINT,
    created_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at         TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (product_id, retailer_id)
);

--rollback DROP TABLE product_cards
