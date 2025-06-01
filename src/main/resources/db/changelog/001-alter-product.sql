--liquibase formatted sql

--changeset iorhael:1748719417199
ALTER TABLE products ADD COLUMN image_link varchar(255);

--rollback ALTER TABLE products DROP COLUMN image_link
