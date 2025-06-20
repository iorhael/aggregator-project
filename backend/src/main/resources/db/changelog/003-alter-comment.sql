--liquibase formatted sql

--changeset iorhael:1750438319876
ALTER TABLE comments ADD CONSTRAINT unique_comment_per_user_per_product UNIQUE (author_id, product_id);

--rollback ALTER TABLE retailers DROP COLUMN logo_link
