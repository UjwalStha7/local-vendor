-- Krishak / local-vendor — database structure only (no data).
-- Import this first: creates database and tables.
--
-- Example (XAMPP):
--   mysql -u root < sql/schema.sql
-- Then import data:
--   mysql -u root < sql/seed.sql

CREATE DATABASE IF NOT EXISTS local_vendor;
USE local_vendor;

-- 1. Users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('admin', 'vendor', 'customer') NOT NULL DEFAULT 'customer',
    is_active BOOLEAN DEFAULT TRUE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Farmer / vendor applications (from /farmer/apply)
CREATE TABLE IF NOT EXISTS vendor_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    applicant_name VARCHAR(120) NOT NULL,
    farm_name VARCHAR(160) NOT NULL,
    contact_email VARCHAR(100) NOT NULL,
    phone VARCHAR(30) NOT NULL,
    category VARCHAR(50),
    about_text TEXT,
    status ENUM('pending', 'contacted', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
    vendor_user_id INT NULL,
    vendor_login_email VARCHAR(100) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE SET NULL
);

-- 2. Vendor profiles
CREATE TABLE IF NOT EXISTS vendor_profiles (
    vendor_user_id INT PRIMARY KEY,
    business_name VARCHAR(100) NOT NULL,
    shop_bio TEXT,
    address VARCHAR(255),
    logo_path VARCHAR(255),
    FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Products
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_user_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    stock_quantity INT NOT NULL,
    photo_path VARCHAR(255),
    is_active TINYINT(1) DEFAULT 1,
    is_flagged TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Product change requests (vendor edits awaiting admin approval)
CREATE TABLE IF NOT EXISTS product_moderation_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    vendor_user_id INT NOT NULL,
    status ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
    prev_name VARCHAR(100) NOT NULL,
    prev_category VARCHAR(50) NOT NULL,
    prev_description TEXT,
    prev_price DECIMAL(10, 2) NOT NULL,
    prev_unit VARCHAR(20) NOT NULL,
    prev_stock_quantity INT NOT NULL,
    prev_photo_path VARCHAR(255),
    proposed_name VARCHAR(100) NOT NULL,
    proposed_category VARCHAR(50) NOT NULL,
    proposed_description TEXT,
    proposed_price DECIMAL(10, 2) NOT NULL,
    proposed_unit VARCHAR(20) NOT NULL,
    proposed_stock_quantity INT NOT NULL,
    proposed_photo_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (vendor_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 4. Carts
CREATE TABLE IF NOT EXISTS carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. Cart items
CREATE TABLE IF NOT EXISTS cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

-- 6. Orders
CREATE TABLE IF NOT EXISTS orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status ENUM('pending', 'confirmed', 'shipped', 'delivered', 'cancelled') DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 7. Order items
CREATE TABLE IF NOT EXISTS order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);
