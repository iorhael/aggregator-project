--liquibase formatted sql

--changeset iorhael:1750008942885
ALTER TABLE retailers ADD COLUMN logo_link varchar(255);

--rollback ALTER TABLE retailers DROP COLUMN logo_link
