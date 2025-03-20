--liquibase formatted sql

--changeset iorhael:1741472754886
CREATE TABLE product_categories
(
    product_id  UUID NOT NULL REFERENCES products,
    category_id UUID NOT NULL REFERENCES categories,
    PRIMARY KEY (product_id, category_id)
);

--rollback DROP TABLE product_categories
