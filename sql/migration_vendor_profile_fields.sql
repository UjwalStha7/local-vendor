-- Extend vendor_profiles for shop bio, address, and logo (run once on existing DB)
USE local_vendor;

ALTER TABLE vendor_profiles
    ADD COLUMN shop_bio TEXT NULL AFTER business_name,
    ADD COLUMN address VARCHAR(255) NULL AFTER shop_bio,
    ADD COLUMN logo_path VARCHAR(255) NULL AFTER address;
