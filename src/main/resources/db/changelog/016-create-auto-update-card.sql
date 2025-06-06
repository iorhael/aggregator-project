--liquibase formatted sql

--changeset iorhael:1749152479686
CREATE TABLE auto_update_cards
(
    id                     UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    retailer_id            UUID UNIQUE  NOT NULL REFERENCES retailers ON DELETE CASCADE,
    download_link          VARCHAR(255) NOT NULL,
    verified_products_only BOOLEAN               DEFAULT TRUE,
    created_at             TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at             TIMESTAMPTZ  NOT NULL DEFAULT now()
);

--rollback DROP TABLE auto_update_cards
