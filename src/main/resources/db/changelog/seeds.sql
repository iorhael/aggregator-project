--liquibase formatted sql

--changeset iorhael:1742203729372 labels:seeds

INSERT INTO users(id, username, email)
VALUES ('c765237f-2702-4a88-943d-8bbc2910f803', 'default_admin', 'admin@example.com'),
       ('ecaaa8a2-4100-4a9d-9b6f-f1f44f25fb13', 'default_retailer', 'retailer@example.com'),
       ('41f7f2d7-0f95-4a7f-b19b-3794ebe1a9de', 'retailer_1', 'retailer1@example.com'),
       ('3737e712-400b-4379-989b-919048f674c6', 'retailer_2', 'retailer2@example.com'),
       ('f3315d1c-1ef4-4b61-aac4-ce6bc57a80a6', 'default_author', 'author@example.com'),
       ('ca2d227c-f2df-45b8-acf4-443bc78d0b2b', 'default_user', 'user@example.com');

INSERT INTO retailers(owner_id, name, description, email, website)
VALUES ('f3315d1c-1ef4-4b61-aac4-ce6bc57a80a6', 'electrosila',
        'We sell household electrical appliances at affordable prices', 'electrosila@example.com', 'electrosila.com'),
       ('41f7f2d7-0f95-4a7f-b19b-3794ebe1a9de', '7element',
        'From irons to powerful computers. You''ve come to the right place', '7element@example.com', '7element.com'),
       ('3737e712-400b-4379-989b-919048f674c6', 'store_of_everything', 'We sell everything in the world',
        'streverything@example.com', 'streverything.com');

WITH electrosila_id AS (SELECT id FROM retailers WHERE name = 'electrosila'),
     seventh_element_id AS (SELECT id FROM retailers WHERE name = '7element'),
     store_of_everything_id AS (SELECT id FROM retailers WHERE name = 'store_of_everything'),
     schedule_1 AS (SELECT '{
       "Monday": "9:00 - 18:00",
       "Tuesday": "9:00 - 18:00",
       "Wednesday": "9:00 - 18:00",
       "Thursday": "9:00 - 18:00",
       "Friday": "9:00 - 18:00",
       "Saturday": "10:00 - 16:00",
       "Sunday": "Closed"
     }'::jsonb AS schedule),
     schedule_2 AS (SELECT '{
       "Monday": "9:00 - 18:00",
       "Tuesday": "9:00 - 18:00",
       "Wednesday": "9:00 - 18:00",
       "Thursday": "9:00 - 18:00",
       "Friday": "9:00 - 18:00",
       "Saturday": "Closed",
       "Sunday": "Closed"
     }'::jsonb AS schedule)
INSERT
INTO stores(retailer_id, address, phone, opening_hours)
SELECT id, '123 Main St, Springfield, IL, USA', '15551234567', schedule
FROM electrosila_id,
     schedule_1
UNION ALL
SELECT id, '456 Elm St, Austin, TX, USA', '15552345678', schedule
FROM electrosila_id,
     schedule_2
UNION ALL
SELECT id, '789 Oak St, Denver, CO, USA', '15553456789', schedule
FROM seventh_element_id,
     schedule_1
UNION ALL
SELECT id, '321 Pine St, Seattle, WA, USA', '15554567890', schedule
FROM seventh_element_id,
     schedule_2
UNION ALL
SELECT id, '654 Maple St, Miami, FL, USA', '15555678901', schedule
FROM store_of_everything_id,
     schedule_1
UNION ALL
SELECT id, '987 Birch St, Boston, MA, USA', '15556789012', schedule
FROM store_of_everything_id,
     schedule_2;

INSERT INTO products(name, description, parent_id, creation_type)
VALUES ('Apple iPhone 15 Pro',
        'Flagship smartphone from Apple featuring a 6.1-inch OLED display, A17 Bionic chip, triple 48 MP camera system, and 5G support. Available in Space Black, Silver, Gold, and Deep Purple',
        NULL, 'AUTOMATICALLY_ADDED'),
       ('Dell XPS 13 (2023 Model)',
        'Ultrabook with a 13.4-inch 4K UHD+ display, Intel Core i7 12th Gen processor, 16 GB RAM, and 512 GB SSD. Lightweight (1.2 kg) and compact, perfect for work and travel',
        NULL, 'AUTOMATICALLY_ADDED'),
       ('Sony WH-1000XM5',
        'Wireless noise-canceling headphones with 30-hour battery life and support for high-quality audio via LDAC. Ideal for work, travel, and music listening',
        NULL, 'AUTOMATICALLY_ADDED'),
       ('Samsung Galaxy Watch 6 Classic',
        'Smartwatch with a 1.4-inch round AMOLED display, Android and iOS compatibility, heart rate monitoring, SpO2 tracking, and GPS. Water-resistant up to 5 ATM, with up to 40 hours of battery life',
        NULL, 'AUTOMATICALLY_ADDED'),
       ('The North Face McMurdo Parka III',
        'Warm winter jacket with ThermoBall™ Eco insulation, waterproof coating, and a fur-lined hood. Designed for extreme cold weather. Available in black, blue, and green',
        NULL, 'AUTOMATICALLY_ADDED'),
       ('Nike Air Max 270 React',
        'Step into comfort and style with the Nike Air Max 270 React. Designed for runners and casual wearers alike, these shoes feature a lightweight React foam midsole for responsive cushioning and a Max Air unit for unparalleled comfort. The breathable mesh upper ensures all-day ventilation, while the durable rubber outsole provides excellent traction. Available in multiple colors to match your style. Perfect for running, gym workouts, or everyday wear',
        NULL, 'AUTOMATICALLY_ADDED');
INSERT INTO products(name, description, parent_id, creation_type)
VALUES ('Premium Noise-Canceling Wireless Headphones XM5 by Sony',
        'Experience unparalleled sound quality with the Sony XM5 Wireless Headphones. Designed for those who demand the best, these headphones feature advanced noise-canceling technology, 30-hour battery life, and seamless Bluetooth connectivity. Perfect for music lovers, frequent travelers, and professionals who need focus in any environment. Elevate your audio experience with Sony''s cutting-edge technology',
        (SELECT id FROM products WHERE name = 'Sony WH-1000XM5'), 'CUSTOM'),
       ('Pro Fitness Smartwatch Galaxy 6 Classic by Samsung',
        'Stay connected and in control with the Samsung Galaxy 6 Classic Smartwatch. Designed for fitness enthusiasts and professionals, this smartwatch features a sleek round AMOLED display, advanced health monitoring (heart rate, SpO2, sleep tracking), and built-in GPS. With up to 40 hours of battery life and water resistance up to 5 ATM, it’s perfect for workouts, outdoor adventures, and everyday use. Elevate your lifestyle with Samsung’s cutting-edge wearable technology',
        (SELECT id FROM products WHERE name = 'Samsung Galaxy Watch 6 Classic'), 'CUSTOM');

INSERT INTO categories(name, description, parent_id)
VALUES ('Electronics', 'Devices and gadgets for everyday use', NULL),
       ('Clothing', 'Apparel for men, women, and kids', NULL);

WITH electronics_id AS (SELECT id FROM categories WHERE name = 'Electronics'),
     clothing_id AS (SELECT id FROM categories WHERE name = 'Clothing')
INSERT
INTO categories(name, description, parent_id)
SELECT 'Smartphones', 'Mobile phones with advanced features', id
FROM electronics_id
UNION ALL
SELECT 'Laptops', 'Portable computers for work and entertainment', id
FROM electronics_id
UNION ALL
SELECT 'Wearables', 'Smart devices worn on the body', id
FROM electronics_id
UNION ALL
SELECT 'Audio', 'Headphones, speakers, and other audio devices', id
FROM electronics_id
UNION ALL
SELECT 'Outerwear', 'Jackets, coats, and other outer garments', id
FROM clothing_id
UNION ALL
SELECT 'Activewear', 'Clothing designed for sports and physical activities', id
FROM clothing_id;

INSERT INTO categories(name, description, parent_id)
VALUES ('Running shoes', 'Footwear designed for running', (SELECT id FROM categories WHERE name = 'Activewear'));

INSERT INTO product_categories(product_id, category_id)
VALUES ((SELECT id FROM products WHERE name = 'Apple iPhone 15 Pro'),
        (SELECT id FROM categories WHERE name = 'Smartphones')),
       ((SELECT id FROM products WHERE name = 'Dell XPS 13 (2023 Model)'),
        (SELECT id FROM categories WHERE name = 'Laptops')),
       ((SELECT id FROM products WHERE name = 'Sony WH-1000XM5'), (SELECT id FROM categories WHERE name = 'Audio')),
       ((SELECT id FROM products WHERE name = 'Samsung Galaxy Watch 6 Classic'),
        (SELECT id FROM categories WHERE name = 'Wearables')),
       ((SELECT id FROM products WHERE name = 'The North Face McMurdo Parka III'),
        (SELECT id FROM categories WHERE name = 'Outerwear')),
       ((SELECT id FROM products WHERE name = 'Nike Air Max 270 React'),
        (SELECT id FROM categories WHERE name = 'Running shoes'));

WITH electrosila_id AS (SELECT id FROM retailers WHERE name = 'electrosila'),
     seventh_element_id AS (SELECT id FROM retailers WHERE name = '7element'),
     store_of_everything_id AS (SELECT id FROM retailers WHERE name = 'store_of_everything'),

     iphone_id AS (SELECT id FROM products WHERE name = 'Apple iPhone 15 Pro'),
     dell_xps_id AS (SELECT id FROM products WHERE name = 'Dell XPS 13 (2023 Model)'),
     sony_xm5_id AS (SELECT id FROM products WHERE name = 'Sony WH-1000XM5'),
     custom_sony_xm5_id AS (SELECT id
                            FROM products
                            WHERE name = 'Premium Noise-Canceling Wireless Headphones XM5 by Sony'),
     samsung_watch_id AS (SELECT id FROM products WHERE name = 'Samsung Galaxy Watch 6 Classic'),
     custom_samsung_watch_id AS (SELECT id
                                 FROM products
                                 WHERE name = 'Pro Fitness Smartwatch Galaxy 6 Classic by Samsung'),
     north_face_id AS (SELECT id FROM products WHERE name = 'The North Face McMurdo Parka III'),
     nike_air_max_id AS (SELECT id FROM products WHERE name = 'Nike Air Max 270 React'),

     iphone_props AS (SELECT '{
       "discount": "10%",
       "installment_available": true,
       "warranty": "1 year",
       "color_options": [
         "Space Black",
         "Silver",
         "Gold",
         "Deep Purple"
       ]
     }'::jsonb AS additional_properties),
     dell_xps_props AS (SELECT '{
       "discount": "5%",
       "installment_available": true,
       "warranty": "2 years",
       "ram_options": [
         "16GB",
         "32GB"
       ]
     }'::jsonb AS additional_properties),
     sony_xm5_props AS (SELECT '{
       "discount": "15%",
       "installment_available": false,
       "warranty": "1 year",
       "color_options": [
         "Black",
         "Silver"
       ]
     }'::jsonb AS additional_properties),
     samsung_watch_props AS (SELECT '{
       "discount": "0%",
       "installment_available": true,
       "warranty": "1 year",
       "strap_options": [
         "Leather",
         "Silicone"
       ]
     }'::jsonb AS additional_properties),
     north_face_props AS (SELECT '{
       "discount": "20%",
       "installment_available": false,
       "warranty": "6 months",
       "size_options": [
         "S",
         "M",
         "L",
         "XL"
       ]
     }'::jsonb AS additional_properties),
     nike_air_max_props AS (SELECT '{
       "discount": "10%",
       "installment_available": true,
       "warranty": "3 months",
       "color_options": [
         "Black",
         "White",
         "Blue"
       ]
     }'::jsonb AS additional_properties)

INSERT
INTO product_cards (product_id, retailer_id, additional_properties)

-- electrosila product_cards (without iphone, with custom sony x5)
SELECT dell_xps_id.id, electrosila_id.id, dell_xps_props.additional_properties
FROM dell_xps_id,
     electrosila_id,
     dell_xps_props
UNION ALL
SELECT custom_sony_xm5_id.id, electrosila_id.id, sony_xm5_props.additional_properties
FROM custom_sony_xm5_id,
     electrosila_id,
     sony_xm5_props
UNION ALL
SELECT samsung_watch_id.id, electrosila_id.id, samsung_watch_props.additional_properties
FROM samsung_watch_id,
     electrosila_id,
     samsung_watch_props
UNION ALL

-- 7element product_cards (without dell xps, with custom samsung watch)
SELECT iphone_id.id, seventh_element_id.id, iphone_props.additional_properties
FROM iphone_id,
     seventh_element_id,
     iphone_props
UNION ALL
SELECT sony_xm5_id.id, seventh_element_id.id, dell_xps_props.additional_properties
FROM sony_xm5_id,
     seventh_element_id,
     dell_xps_props
UNION ALL
SELECT custom_samsung_watch_id.id, seventh_element_id.id, samsung_watch_props.additional_properties
FROM custom_samsung_watch_id,
     seventh_element_id,
     samsung_watch_props
UNION ALL

-- store_of_everything product_cards (all products, without custom)
SELECT iphone_id.id, store_of_everything_id.id, iphone_props.additional_properties
FROM iphone_id,
     store_of_everything_id,
     iphone_props
UNION ALL
SELECT dell_xps_id.id, store_of_everything_id.id, dell_xps_props.additional_properties
FROM dell_xps_id,
     store_of_everything_id,
     dell_xps_props
UNION ALL
SELECT sony_xm5_id.id, store_of_everything_id.id, sony_xm5_props.additional_properties
FROM sony_xm5_id,
     store_of_everything_id,
     sony_xm5_props
UNION ALL
SELECT samsung_watch_id.id, store_of_everything_id.id, samsung_watch_props.additional_properties
FROM samsung_watch_id,
     store_of_everything_id,
     samsung_watch_props
UNION ALL
SELECT north_face_id.id, store_of_everything_id.id, north_face_props.additional_properties
FROM north_face_id,
     store_of_everything_id,
     north_face_props
UNION ALL
SELECT nike_air_max_id.id, store_of_everything_id.id, nike_air_max_props.additional_properties
FROM nike_air_max_id,
     store_of_everything_id,
     nike_air_max_props;

INSERT INTO price_histories (card_id, price, updated_at)
SELECT pc.id                                  AS card_id,
       CASE
           WHEN p.name = 'Apple iPhone 15 Pro' THEN ROUND((random() * 200 + 800)::numeric, 2) -- Price from 800 to 1000
           WHEN p.name = 'Dell XPS 13 (2023 Model)' THEN ROUND((random() * 300 + 1200)::numeric, 2) -- Price from 1200 to 1500
           WHEN p.name = 'Sony WH-1000XM5' THEN ROUND((random() * 100 + 300)::numeric, 2) -- Price from 300 to 400
           ELSE ROUND((random() * 1000 + 100)::numeric, 2) -- Default price
           END                                AS price,
       NOW() - (random() * interval '7 days') AS updated_at
FROM product_cards pc
         JOIN products p ON pc.product_id = p.id
         CROSS JOIN generate_series(1, 2);

/* liquibase rollback
   rollback TRUNCATE users CASCADE
   rollback TRUNCATE retailers CASCADE
   rollback TRUNCATE stores CASCADE
   rollback TRUNCATE products CASCADE
   rollback TRUNCATE product_cards CASCADE
   rollback TRUNCATE price_histories CASCADE
   rollback TRUNCATE categories CASCADE
   rollback TRUNCATE product_categories CASCADE
 */
