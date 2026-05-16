-- Krishak storefront demo data (optional — run after schema.sql on a dev database).
-- Removes prior seed rows for these vendor IDs, then inserts vendors + products.
USE farmers_market_hub;

-- Password hash is for: SeedPass123 (jBCrypt)

DELETE FROM products WHERE vendor_user_id IN (9101, 9102, 9103);

DELETE FROM vendor_profiles WHERE vendor_user_id IN (9101, 9102, 9103);

DELETE FROM users WHERE id IN (9101, 9102, 9103);

INSERT INTO users (id, email, password, username, phone, role, is_active) VALUES
(9101, 'green.valley@seed.krishak',
 '$2a$10$e6xu7AQZns9zGNc7ALMVC.ONsTx2sPBPmXliuhwma1obZY9EXtBvK',
 'gv_seed_user', '9801111111', 'vendor', 1),
(9102, 'organic.harvest@seed.krishak',
 '$2a$10$e6xu7AQZns9zGNc7ALMVC.ONsTx2sPBPmXliuhwma1obZY9EXtBvK',
 'oh_seed_user', '9802222222', 'vendor', 1),
(9103, 'fresh.fields@seed.krishak',
 '$2a$10$e6xu7AQZns9zGNc7ALMVC.ONsTx2sPBPmXliuhwma1obZY9EXtBvK',
 'ff_seed_user', '9803333333', 'vendor', 1);

INSERT INTO vendor_profiles (vendor_user_id, business_name) VALUES
(9101, 'Green Valley Farm'),
(9102, 'Organic Harvest'),
(9103, 'Fresh Fields');

INSERT INTO products (vendor_user_id, name, category, description, price, unit, stock_quantity, photo_path, is_active, is_flagged) VALUES
(9101, 'Fresh Tomatoes', 'Vegetables', 'Organic vine-ripened tomatoes.', 120.00, 'kg', 50, 'image/hero.svg', 1, 0),
(9102, 'Organic Carrots', 'Vegetables', 'Sweet crunchy organic carrots.', 90.00, 'kg', 4, 'image/hero.svg', 1, 0),
(9103, 'Fresh Strawberries', 'Fruits', 'Juicy seasonal strawberries.', 220.00, 'kg', 25, 'image/fruit.svg', 1, 0),
(9101, 'Bell Peppers', 'Vegetables', 'Colorful sweet bell peppers.', 180.00, 'kg', 8, 'image/organic_vegetables.svg', 1, 0),
(9102, 'Fresh Apples', 'Fruits', 'Crisp mountain apples.', 160.00, 'kg', 40, 'image/fruit.svg', 1, 0),
(9103, 'Mixed Vegetables', 'Vegetables', 'Seasonal mixed vegetable pack.', 140.00, 'kg', 15, 'image/organic_vegetables.svg', 1, 0),
(9101, 'Fresh Oranges', 'Fruits', 'Vitamin-rich citrus oranges.', 130.00, 'kg', 30, 'image/fruit.svg', 1, 0),
(9102, 'Green Cucumbers', 'Vegetables', 'Farm cucumbers perfect for salads.', 75.00, 'kg', 12, 'image/organic_vegetables.svg', 1, 0),
(9103, 'Fresh Citrus Mix', 'Fruits', 'Assorted lemons, limes, and oranges.', 200.00, 'kg', 6, 'image/fruit.svg', 1, 0);
