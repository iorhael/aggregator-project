--liquibase formatted sql

--changeset iorhael:1741471690108
CREATE TABLE price_histories
(
    id         UUID PRIMARY KEY       DEFAULT gen_random_uuid(),
    card_id    UUID          NOT NULL REFERENCES product_cards,
    price      DECIMAL(9, 2) NOT NULL CHECK (price > 0),
    updated_at TIMESTAMPTZ   NOT NULL DEFAULT now()
);

--rollback DROP TABLE price_histories
