--liquibase formatted sql

--changeset iorhael:1750440115622
CREATE TABLE temp_review_images
(
    id         UUID PRIMARY KEY      DEFAULT gen_random_uuid(),
    image_link VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT now()
);

--rollback DROP TABLE temp_review_images
