--liquibase formatted sql

--changeset iorhael:1741471456552
CREATE TABLE favorites
(
    id         UUID PRIMARY KEY     DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL REFERENCES users,
    product_id UUID        NOT NULL REFERENCES products,
    notes      TEXT,
    user_tag   VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (user_id, product_id)
);

--rollback DROP TABLE favorites
