--liquibase formatted sql

--changeset iorhael:1741472386607
CREATE TABLE comments
(
    id             UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    author_id      UUID        REFERENCES users ON DELETE SET NULL,
    product_id     UUID REFERENCES products,
    content        TEXT        NOT NULL,
    product_rating SMALLINT    NOT NULL CHECK (product_rating > 1 AND product_rating <= 5),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

--rollback DROP TABLE comments
