--liquibase formatted sql

--changeset iorhael:1748815555570
CREATE TABLE used_seeds
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    seed_name VARCHAR(50) NOT NULL
);

--rollback DROP TABLE used_seeds
