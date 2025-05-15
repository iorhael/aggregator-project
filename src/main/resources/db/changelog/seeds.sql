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
VALUES ('ecaaa8a2-4100-4a9d-9b6f-f1f44f25fb13', 'electrosila',
        'We sell household electrical appliances at affordable prices', 'electrosila@example.com', 'electrosila.com'),
       ('41f7f2d7-0f95-4a7f-b19b-3794ebe1a9de', '7element',
        'From irons to powerful computers. You''ve come to the right place', '7element@example.com', '7element.com'),
       ('3737e712-400b-4379-989b-919048f674c6', 'store_of_everything', 'We sell everything in the world',
        'streverything@example.com', 'streverything.com');

WITH electrosila_id AS (SELECT id FROM retailers WHERE name = 'electrosila'),
     seventh_element_id AS (SELECT id FROM retailers WHERE name = '7element'),
     store_of_everything_id AS (SELECT id FROM retailers WHERE name = 'store_of_everything'),
     schedule_1 AS (SELECT '{
       "monday": "9:00 - 18:00",
       "tuesday": "9:00 - 18:00",
       "wednesday": "9:00 - 18:00",
       "thursday": "9:00 - 18:00",
       "friday": "9:00 - 18:00",
       "saturday": "10:00 - 16:00",
       "sunday": "Closed"
     }'::jsonb AS schedule),
     schedule_2 AS (SELECT '{
       "monday": "9:00 - 18:00",
       "tuesday": "9:00 - 18:00",
       "wednesday": "9:00 - 18:00",
       "thursday": "9:00 - 18:00",
       "friday": "9:00 - 18:00",
       "saturday": "Closed",
       "sunday": "Closed"
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

INSERT INTO vendors(name, description)
VALUES ('Apple',
        'Innovative technology company known for premium smartphones, computers, and wearables, combining sleek design with cutting-edge performance'),
       ('Dell',
        'Leading computer manufacturer specializing in high-performance laptops, desktops, and enterprise solutions for professionals and creatives'),
       ('Sony',
        'Global electronics giant renowned for audio devices, gaming consoles, and entertainment technology, delivering exceptional sound and imaging quality'),
       ('Samsung',
        'South Korean tech powerhouse producing smartphones, wearables, and home appliances, known for innovation and high-quality displays'),
       ('The North Face',
        'Outdoor apparel and gear brand focused on durability and performance, trusted by adventurers for extreme weather conditions'),
       ('Nike',
        'World’s most iconic sportswear brand, designing athletic shoes and apparel with advanced technology for athletes and casual wearers alike');

INSERT INTO products(vendor_id, name, description, characteristics, verified)
VALUES ((SELECT id FROM vendors WHERE name = 'Apple'),
        'Apple iPhone 15 Pro',
        'Flagship smartphone from Apple featuring a 6.1-inch OLED display, A17 Bionic chip, triple 48 MP camera system, and 5G support. Available in Space Black, Silver, Gold, and Deep Purple',
        '{
          "general": {
            "type": "smartphone",
            "purpose": "mobile communication, multimedia",
            "color": "titanium black",
            "size": "146.6 x 70.6 x 8.25 mm"
          },
          "specs": {
            "display": "\"6.1\" Super Retina XDR",
            "processor": "A17 Pro",
            "storage": "128GB/256GB/512GB/1TB",
            "camera": "Triple 48MP + 12MP + 12MP",
            "battery": "3274 mAh"
          },
          "weight": {
            "net": "187 g",
            "with_package": "220 g"
          }
        }'::jsonb, TRUE),
       ((SELECT id FROM vendors WHERE name = 'Dell'),
        'Dell XPS 13 (2023 Model)',
        'Ultrabook with a 13.4-inch 4K UHD+ display, Intel Core i7 12th Gen processor, 16 GB RAM, and 512 GB SSD. Lightweight (1.2 kg) and compact, perfect for work and travel',
        '{
          "general": {
            "type": "laptop",
            "purpose": "work, entertainment",
            "color": "platinum silver",
            "size": "295.3 x 199.04 x 13.99 mm"
          },
          "specs": {
            "display": "13.4\" 4K UHD+ Touch",
            "processor": "Intel Core i7-1360P",
            "ram": "16GB LPDDR5",
            "storage": "512GB SSD",
            "os": "Windows 11 Pro"
          },
          "weight": {
            "net": "1.17 kg",
            "with_package": "1.8 kg"
          }
        }'::jsonb, TRUE),
       ((SELECT id FROM vendors WHERE name = 'Sony'),
        'Sony WH-1000XM5',
        'Wireless noise-canceling headphones with 30-hour battery life and support for high-quality audio via LDAC. Ideal for work, travel, and music listening',
        '{
          "general": {
            "type": "headphones",
            "purpose": "wireless noise-canceling",
            "color": "black",
            "size": "foldable design"
          },
          "specs": {
            "driver": "30mm dynamic",
            "battery": "30 hours (NC on)",
            "bluetooth": "5.2",
            "microphones": "8 for noise canceling"
          },
          "weight": {
            "net": "250 g",
            "with_case": "320 g"
          }
        }'::jsonb, TRUE),
       ((SELECT id FROM vendors WHERE name = 'Samsung'),
        'Samsung Galaxy Watch 6 Classic',
        'Smartwatch with a 1.4-inch round AMOLED display, Android and iOS compatibility, heart rate monitoring, SpO2 tracking, and GPS. Water-resistant up to 5 ATM, with up to 40 hours of battery life',
        '{
          "general": {
            "type": "smartwatch",
            "purpose": "fitness tracking, notifications",
            "color": "silver",
            "size": "42mm/46mm case"
          },
          "specs": {
            "display": "1.5\" Super AMOLED",
            "os": "Wear OS",
            "sensors": "heart rate, blood pressure, ECG",
            "waterproof": "5ATM + IP68"
          },
          "weight": {
            "net": "59 g (42mm)",
            "with_strap": "85 g"
          }
        }'::jsonb, TRUE),
       ((SELECT id FROM vendors WHERE name = 'The North Face'),
        'The North Face McMurdo Parka III',
        'Warm winter jacket with ThermoBall™ Eco insulation, waterproof coating, and a fur-lined hood. Designed for extreme cold weather. Available in black, blue, and green',
        '{
          "general": {
            "type": "winter jacket",
            "purpose": "extreme cold protection",
            "color": "black",
            "size": "XS-XXL"
          },
          "materials": {
            "outer": "recycled polyester",
            "insulation": "600-fill goose down",
            "lining": "recycled nylon"
          },
          "weight": {
            "net": "1.4 kg (size M)",
            "with_package": "1.7 kg"
          }
        }'::jsonb, TRUE),
       ((SELECT id FROM vendors WHERE name = 'Nike'),
        'Nike Air Max 270 React',
        'Step into comfort and style with the Nike Air Max 270 React. Designed for runners and casual wearers alike, these shoes feature a lightweight React foam midsole for responsive cushioning and a Max Air unit for unparalleled comfort. The breathable mesh upper ensures all-day ventilation, while the durable rubber outsole provides excellent traction. Available in multiple colors to match your style. Perfect for running, gym workouts, or everyday wear',
        '{
          "general": {
            "type": "sneakers",
            "purpose": "running, casual",
            "color": "white/black/red",
            "size": "US 5-13"
          },
          "specs": {
            "upper": "breathable mesh",
            "sole": "Nike React foam + Air Max unit",
            "closure": "lace-up"
          },
          "weight": {
            "net": "310 g (per shoe size 9)",
            "pair_with_box": "850 g"
          }
        }'::jsonb, TRUE);

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
     samsung_watch_id AS (SELECT id FROM products WHERE name = 'Samsung Galaxy Watch 6 Classic'),
     north_face_id AS (SELECT id FROM products WHERE name = 'The North Face McMurdo Parka III'),
     nike_air_max_id AS (SELECT id FROM products WHERE name = 'Nike Air Max 270 React')
INSERT
INTO product_cards (id, product_id, retailer_id, description, warranty, installment_period, max_delivery_time)

-- electrosila product_cards (without iphone, with custom sony x5)
SELECT gen_random_uuid(), dell_xps_id.id, electrosila_id.id, 'Test description', 10, 12, 3
FROM dell_xps_id,
     electrosila_id
UNION ALL
SELECT gen_random_uuid(), sony_xm5_id.id, electrosila_id.id, 'Test description', 8, NULL, NULL
FROM sony_xm5_id,
     electrosila_id
UNION ALL
SELECT gen_random_uuid(), samsung_watch_id.id, electrosila_id.id, 'Test description', 2, 2, 2
FROM samsung_watch_id,
     electrosila_id
UNION ALL

-- 7element product_cards (without dell xps, with custom samsung watch)
SELECT gen_random_uuid(), iphone_id.id, seventh_element_id.id, 'Test description', 6, 6, 4
FROM iphone_id,
     seventh_element_id
UNION ALL
SELECT gen_random_uuid(), sony_xm5_id.id, seventh_element_id.id, 'Test description', 6, 6, 2
FROM sony_xm5_id,
     seventh_element_id
UNION ALL
SELECT gen_random_uuid(), samsung_watch_id.id, seventh_element_id.id, 'Test description', NULL, 4, 3
FROM samsung_watch_id,
     seventh_element_id
UNION ALL

-- store_of_everything product_cards (all products, without custom)
SELECT gen_random_uuid(), iphone_id.id, store_of_everything_id.id, 'Test description', 2, 2, 5
FROM iphone_id,
     store_of_everything_id
UNION ALL
SELECT gen_random_uuid(), dell_xps_id.id, store_of_everything_id.id, 'Test description', 5, 5, 10
FROM dell_xps_id,
     store_of_everything_id
UNION ALL
SELECT gen_random_uuid(), sony_xm5_id.id, store_of_everything_id.id, 'Test description', 1, NULL, 1
FROM sony_xm5_id,
     store_of_everything_id
UNION ALL
SELECT gen_random_uuid(), samsung_watch_id.id, store_of_everything_id.id, 'Test description', 10, 10, NULL
FROM samsung_watch_id,
     store_of_everything_id
UNION ALL
SELECT gen_random_uuid(), north_face_id.id, store_of_everything_id.id, 'Test description', 10, 10, 20
FROM north_face_id,
     store_of_everything_id
UNION ALL
SELECT gen_random_uuid(), nike_air_max_id.id, store_of_everything_id.id, 'Test description', NULL, NULL, NULL
FROM nike_air_max_id,
     store_of_everything_id;

INSERT INTO price_histories (id, card_id, price, updated_at)
SELECT gen_random_uuid() as id,
       pc.id                                  AS card_id,
       CASE
           WHEN p.name = 'Apple iPhone 15 Pro' THEN ROUND((random() * 200 + 800)::numeric, 2) -- Price from 800 to 1000
           WHEN p.name = 'Dell XPS 13 (2023 Model)'
               THEN ROUND((random() * 300 + 1200)::numeric, 2) -- Price from 1200 to 1500
           WHEN p.name = 'Sony WH-1000XM5' THEN ROUND((random() * 100 + 300)::numeric, 2) -- Price from 300 to 400
           ELSE ROUND((random() * 1000 + 100)::numeric, 2) -- Default price
           END                                AS price,
       NOW() - (random() * interval '7 days') AS updated_at
FROM product_cards pc
         JOIN products p ON pc.product_id = p.id
         CROSS JOIN generate_series(1, 2);

WITH default_user AS (SELECT id FROM users WHERE username = 'default_user'),
     products AS (SELECT id, name
                  FROM products
                  WHERE name IN (
                                 'Apple iPhone 15 Pro',
                                 'Dell XPS 13 (2023 Model)',
                                 'Sony WH-1000XM5',
                                 'Samsung Galaxy Watch 6 Classic',
                                 'The North Face McMurdo Parka III',
                                 'Nike Air Max 270 React'
                      ))

INSERT
INTO comments (author_id, product_id, content, product_rating)
SELECT (SELECT id FROM default_user) AS author_id,
       p.id                          AS product_id,
       CASE
           WHEN p.name = 'Apple iPhone 15 Pro' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'Great phone, the camera is amazing!'
                   WHEN sub.row_num = 2 THEN 'Fast performance and excellent display.'
                   WHEN sub.row_num = 3 THEN 'A bit pricey but the quality is top-notch.'
                   END
           WHEN p.name = 'Dell XPS 13 (2023 Model)' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'The best laptop for work, very lightweight.'
                   WHEN sub.row_num = 2 THEN 'The screen is gorgeous, battery lasts long.'
                   WHEN sub.row_num = 3 THEN 'Gets a bit noisy under load but overall excellent.'
                   END
           WHEN p.name = 'Sony WH-1000XM5' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'Best noise-canceling headphones ever!'
                   WHEN sub.row_num = 2 THEN 'Very comfortable with clean, deep sound.'
                   WHEN sub.row_num = 3 THEN 'Expensive but worth every penny.'
                   END
           WHEN p.name = 'Samsung Galaxy Watch 6 Classic' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'Excellent smartwatch with many features.'
                   WHEN sub.row_num = 2 THEN 'Battery life could be better.'
                   WHEN sub.row_num = 3 THEN 'Stylish design, intuitive interface.'
                   END
           WHEN p.name = 'The North Face McMurdo Parka III' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'Extremely warm jacket, fits perfectly.'
                   WHEN sub.row_num = 2 THEN 'High-quality craftsmanship.'
                   WHEN sub.row_num = 3 THEN 'A bit heavy but keeps you very warm.'
                   END
           WHEN p.name = 'Nike Air Max 270 React' THEN
               CASE
                   WHEN sub.row_num = 1 THEN 'Very comfortable sneakers!'
                   WHEN sub.row_num = 2 THEN 'Stylish and lightweight, great for everyday wear.'
                   WHEN sub.row_num = 3 THEN 'The sole could be a bit softer.'
                   END
           END                       AS content,
       CASE
           WHEN sub.row_num = 1 THEN 5
           WHEN sub.row_num = 2 THEN 4
           WHEN sub.row_num = 3 THEN 4
           END                       AS product_rating
FROM products p
         CROSS JOIN
         (SELECT 1 AS row_num UNION SELECT 2 UNION SELECT 3) AS sub
ORDER BY p.id, sub.row_num;

/* liquibase rollback
   rollback TRUNCATE users CASCADE
   rollback TRUNCATE retailers CASCADE
   rollback TRUNCATE stores CASCADE
   rollback TRUNCATE products CASCADE
   rollback TRUNCATE product_cards CASCADE
   rollback TRUNCATE price_histories CASCADE
   rollback TRUNCATE categories CASCADE
   rollback TRUNCATE vendors CASCADE
   rollback TRUNCATE product_categories CASCADE
   rollback TRUNCATE comments CASCADE
 */
