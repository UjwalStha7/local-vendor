-- Run on existing local_vendor database
USE local_vendor;

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
