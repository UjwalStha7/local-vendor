-- Run on existing local_vendor database if you already created tables before this feature.
USE local_vendor;

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
