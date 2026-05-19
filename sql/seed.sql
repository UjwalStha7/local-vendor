-- Krishak / local-vendor — seed data (import after schema.sql).
--
-- Example (XAMPP):
--   mysql -u root < sql/schema.sql
--   mysql -u root < sql/seed.sql
--
-- Logins (passwords are bcrypt-hashed in DB):
--   Admin:    krishakadmin@krishak.np  (also bootstraps on first login with default admin password)
--   Customer: gurungaryan17@gmail.com, kushalshrestha933@gmail.com
--   Vendor:   kushalshrestha933@krishak.np, wassa@krishak.np
--
-- Note: Uploaded product/logo files live under ~/local-vendor-uploads on the server;
--       photo_path values reference those filenames.

USE local_vendor;

SET FOREIGN_KEY_CHECKS = 0;

-- Users
INSERT INTO users (id, email, password, username, phone, role, is_active, createdAt, updatedAt) VALUES
(1, 'gurungaryan17@gmail.com', '$2a$10$tcEo24dSf/vaUeGySsgJieazkxKyEam5AASaG4cXKl/HnS8t4R5vu', 'jongrg', '9800776655', 'customer', 1, '2026-05-18 07:24:41', '2026-05-18 07:24:41'),
(3, 'krishakadmin@krishak.np', '$2a$10$tPQjOvXIPp5RjfcVxg1VmeAM1Q8C4.eiM5xgdHC.VcpXEQaSfAe0a', 'Krishak Admin', '', 'admin', 1, '2026-05-19 12:58:02', '2026-05-19 12:58:02'),
(4, 'kushalshrestha933@gmail.com', '$2a$10$GxnLRzJHmQJ5w9QW5j8O0OsC6SFNCaguL4CJx0nNu18XEo5zrCdb.', 'Kushal', '9815155865', 'customer', 1, '2026-05-19 12:58:48', '2026-05-19 12:58:48'),
(5, 'kushalshrestha933@krishak.np', '$2a$10$fU70zPsnk/nlYcS4vhb04u/VXC.MmSeRZk2jPX41SvPHVZgQEdzSS', 'Kushal Shrestha', '9822723109', 'vendor', 1, '2026-05-19 13:01:12', '2026-05-19 13:43:55'),
(6, 'wassa@krishak.np', '$2a$10$u6DaDsAvpGtx4yRuvXGDAupe7vKkcrjuuu7Vrop/BCAjPoOKYWmVO', 'Shirsh Wassa', '98765 43210', 'vendor', 1, '2026-05-19 16:42:04', '2026-05-19 16:42:04');

-- Vendor applications
INSERT INTO vendor_requests (id, applicant_name, farm_name, contact_email, phone, category, about_text, status, vendor_user_id, vendor_login_email, created_at, updated_at) VALUES
(1, 'Kushal Shrestha', 'KushalNFriendsCompany', 'kushalshrestha933@gmail.com', '98227 86303', 'fruits', NULL, 'approved', 5, 'kushalshrestha933@krishak.np', '2026-05-19 12:59:39', '2026-05-19 13:01:13'),
(2, 'Shirsh Wassa', 'We sell', 'wassa@gmail.com', '98765 43210', 'fruits', NULL, 'approved', 6, 'wassa@krishak.np', '2026-05-19 16:41:15', '2026-05-19 16:42:05');

-- Vendor profiles
INSERT INTO vendor_profiles (vendor_user_id, business_name, shop_bio, address, logo_path) VALUES
(5, 'KushalNFriendsCompanys', 'sadf', 'kalesti', '2026-05-19T19-49-41.471168300_logo_20250316.jpg'),
(6, 'We sell', NULL, NULL, NULL);

-- Products
INSERT INTO products (id, vendor_user_id, name, category, description, price, unit, stock_quantity, photo_path, is_active, is_flagged, created_at) VALUES
(1, 5, 'Apple', 'Fruits', 'It is a seasonal fruit.', 50.00, 'kg', 14, '/image/products/product_a4f239ae57ad4c3f87051e2ec927c45d.jpg', 1, 0, '2026-05-19 13:02:51'),
(2, 5, 'Strawberry', 'Vegetables', 'zsxcz', 20.00, 'kg', 17, '2026-05-19T20-48-37.980756900_product_20250316.jpg', 1, 0, '2026-05-19 15:20:55'),
(3, 5, 'Spinach', 'Vegetables', 'Green leafy spinach rich in iron and nutrients.', 60.00, 'bundle', 120, NULL, 1, 0, '2026-05-19 16:32:29'),
(4, 5, 'Cauliflower', 'Vegetables', 'Fresh white cauliflower harvested locally.', 160.00, 'piece', 73, NULL, 1, 0, '2026-05-19 16:32:30'),
(5, 5, 'Carrot', 'Vegetables', 'Crunchy orange carrots full of beta carotene.', 140.00, 'kg', 98, NULL, 1, 0, '2026-05-19 16:32:31'),
(6, 5, 'Onion', 'Vegetables', 'Fresh onions with strong flavor and long shelf life.', 100.00, 'kg', 220, NULL, 1, 0, '2026-05-19 16:32:31'),
(7, 5, 'Potato', 'Vegetables', 'Organic farm potatoes suitable for daily cooking.', 80.00, 'kg', 291, NULL, 1, 0, '2026-05-19 16:32:32'),
(8, 5, 'Tomato', 'Vegetables', 'Fresh red tomatoes rich in vitamins and antioxidants.', 120.00, 'kg', 149, NULL, 1, 0, '2026-05-19 16:32:32'),
(9, 6, 'Patato', 'Vegetables', 'hi', 100.00, 'kg', 2, '2026-05-19T22-28-43.863687700_product_Potato.png', 1, 0, '2026-05-19 16:44:27');

-- Product moderation history
INSERT INTO product_moderation_requests (id, product_id, vendor_user_id, status, prev_name, prev_category, prev_description, prev_price, prev_unit, prev_stock_quantity, prev_photo_path, proposed_name, proposed_category, proposed_description, proposed_price, proposed_unit, proposed_stock_quantity, proposed_photo_path, created_at, reviewed_at) VALUES
(1, 1, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Apple', 'Fruits', 'It is a seasonal fruit', 50.00, 'kg', 19, '/image/products/product_6cecd620d8304152aa33e02b182ed4b0.jpg', '2026-05-19 13:02:30', '2026-05-19 13:02:51'),
(2, 1, 5, 'approved', 'Apple', 'Fruits', 'It is a seasonal fruit', 50.00, 'kg', 19, '/image/products/product_6cecd620d8304152aa33e02b182ed4b0.jpg', 'Apple', 'Fruits', 'It is a seasonal fruit.', 50.00, 'kg', 19, '/image/products/product_6cecd620d8304152aa33e02b182ed4b0.jpg', '2026-05-19 13:03:11', '2026-05-19 13:03:18'),
(3, 1, 5, 'approved', 'Apple', 'Fruits', 'It is a seasonal fruit.', 50.00, 'kg', 19, '/image/products/product_6cecd620d8304152aa33e02b182ed4b0.jpg', 'Apple', 'Fruits', 'It is a seasonal fruit.', 50.00, 'kg', 19, '/image/products/product_a4f239ae57ad4c3f87051e2ec927c45d.jpg', '2026-05-19 13:18:33', '2026-05-19 13:18:42'),
(4, 2, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Strawberry', 'Vegetables', 'zsxcz', 20.00, 'kg', 19, '2026-05-19T20-48-37.980756900_product_20250316.jpg', '2026-05-19 15:03:38', '2026-05-19 15:20:56'),
(5, 8, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Tomato', 'Vegetables', 'Fresh red tomatoes rich in vitamins and antioxidants.', 120.00, 'kg', 150, NULL, '2026-05-19 16:05:44', '2026-05-19 16:32:32'),
(6, 7, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Potato', 'Vegetables', 'Organic farm potatoes suitable for daily cooking.', 80.00, 'kg', 300, NULL, '2026-05-19 16:06:45', '2026-05-19 16:32:32'),
(7, 6, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Onion', 'Vegetables', 'Fresh onions with strong flavor and long shelf life.', 100.00, 'kg', 220, NULL, '2026-05-19 16:07:40', '2026-05-19 16:32:31'),
(8, 5, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Carrot', 'Vegetables', 'Crunchy orange carrots full of beta carotene.', 140.00, 'kg', 100, NULL, '2026-05-19 16:08:39', '2026-05-19 16:32:31'),
(9, 4, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Cauliflower', 'Vegetables', 'Fresh white cauliflower harvested locally.', 160.00, 'piece', 75, NULL, '2026-05-19 16:09:37', '2026-05-19 16:32:30'),
(10, 3, 5, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Spinach', 'Vegetables', 'Green leafy spinach rich in iron and nutrients.', 60.00, 'bundle', 120, NULL, '2026-05-19 16:11:18', '2026-05-19 16:32:29'),
(11, 9, 6, 'approved', '(new)', '—', NULL, 0.00, '—', 0, NULL, 'Patato', 'Vegetables', 'hi', 100.00, 'kg', 4, '2026-05-19T22-28-43.863687700_product_Potato.png', '2026-05-19 16:43:43', '2026-05-19 16:44:27');

-- Orders
INSERT INTO orders (id, user_id, total_price, status, created_at) VALUES
(1, 4, 100.00, 'delivered', '2026-05-19 14:46:05'),
(2, 4, 870.00, 'pending', '2026-05-19 16:33:23'),
(3, 4, 120.00, 'pending', '2026-05-19 16:39:59'),
(4, 4, 100.00, 'delivered', '2026-05-19 16:49:46'),
(5, 4, 740.00, 'pending', '2026-05-19 17:21:22');

-- Order items
INSERT INTO order_items (id, order_id, product_id, quantity, price_at_purchase) VALUES
(1, 1, 1, 2, 50.00),
(2, 2, 1, 3, 50.00),
(3, 2, 2, 2, 20.00),
(4, 2, 5, 2, 140.00),
(5, 2, 4, 2, 160.00),
(6, 2, 7, 1, 80.00),
(7, 3, 8, 1, 120.00),
(8, 4, 9, 1, 100.00),
(9, 5, 9, 1, 100.00),
(10, 5, 7, 8, 80.00);

-- Reset auto-increment counters after explicit IDs
ALTER TABLE users AUTO_INCREMENT = 7;
ALTER TABLE vendor_requests AUTO_INCREMENT = 3;
ALTER TABLE products AUTO_INCREMENT = 10;
ALTER TABLE product_moderation_requests AUTO_INCREMENT = 12;
ALTER TABLE orders AUTO_INCREMENT = 6;
ALTER TABLE order_items AUTO_INCREMENT = 11;

SET FOREIGN_KEY_CHECKS = 1;
