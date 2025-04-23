--liquibase formatted sql

--changeset iorhael:1744553760860
CREATE VIEW products_summary AS
SELECT p.id                                                                                      AS product_id,
       (SELECT COUNT(*) FROM comments WHERE product_id = p.id)                                   AS comments_count,
       (SELECT COUNT(*) FROM product_cards WHERE product_id = p.id)                              AS offers_count,
       ROUND(COALESCE((SELECT AVG(product_rating) FROM comments WHERE product_id = p.id), 0), 2) AS average_rating,
       (SELECT MIN(ph.price)
        FROM product_cards pc
        JOIN LATERAL (
            SELECT price
            FROM price_histories
            WHERE card_id = pc.id
            ORDER BY updated_at DESC
            LIMIT 1
        ) ph ON true
        WHERE pc.product_id = p.id)                                                              AS minimal_price
FROM products p;

--rollback DROP VIEW product_summary;
