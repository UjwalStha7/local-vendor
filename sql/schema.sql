-- Create database
CREATE DATABASE IF NOT EXISTS farmers_market_hub;
USE farmers_market_hub;

-- 1. Users table  (Modified BY Darshan)
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    role ENUM('admin', 'vendor', 'customer') NOT NULL DEFAULT 'customer',
    is_active BOOLEAN DEFAULT TRUE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP

);



-- 2. VendorProfile table
CREATE TABLE vendor_profiles (
    user_id INT PRIMARY KEY,
    business_name VARCHAR(150) NOT NULL,
    description TEXT,
    logo_path VARCHAR(255),
    address VARCHAR(255),
    bank_details VARCHAR(255),
    rating_avg DECIMAL(3,2) DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. Products table
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_user_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    category VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    unit VARCHAR(50),
    stock_quantity INT DEFAULT 0,
    photo_path VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    is_flagged BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_user_id) REFERENCES vendor_profiles(user_id) ON DELETE CASCADE
);

-- 4. Cart table (one per customer)
CREATE TABLE carts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_user_id INT NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 5. CartItem table
CREATE TABLE cart_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cart_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_product (cart_id, product_id)
);

-- 6. Orders table
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_user_id INT NOT NULL,
    vendor_user_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('pending', 'confirmed', 'completed', 'cancelled') DEFAULT 'pending',
    notes TEXT,
    shipping_address VARCHAR(255),
    FOREIGN KEY (customer_user_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (vendor_user_id) REFERENCES vendor_profiles(user_id) ON DELETE RESTRICT
);

-- 7. OrderItems table
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price_at_time DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE RESTRICT
);

-- 8. Reviews table
CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vendor_user_id INT NOT NULL,
    customer_user_id INT NOT NULL,
    order_id INT NOT NULL UNIQUE,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (vendor_user_id) REFERENCES vendor_profiles(user_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- 9. Notifications table
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 10. Subscription table
CREATE TABLE subscriptions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    frequency ENUM('monthly', 'yearly') NOT NULL,
    start_date DATE NOT NULL,
    next_order_date DATE NOT NULL,
    status ENUM('active', 'paused', 'cancelled') DEFAULT 'active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_subscription_customer (customer_user_id),
    INDEX idx_subscription_next_order (next_order_date)
);

-- Indexes for performance
CREATE INDEX idx_products_vendor ON products(vendor_user_id);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_active ON products(is_active);
CREATE INDEX idx_orders_customer ON orders(customer_user_id);
CREATE INDEX idx_orders_vendor ON orders(vendor_user_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_reviews_vendor ON reviews(vendor_user_id);
CREATE INDEX idx_notifications_user ON notifications(user_id);
CREATE INDEX idx_users_role ON users(role);