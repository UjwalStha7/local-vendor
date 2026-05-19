-- Optional seed data for login routing tests (run after schema.sql).
-- Passwords below are examples only — change before production.

USE local_vendor;

-- Admin (also auto-created on first login with krishakadmin@krishak.np / ukdijs0987654321)
-- INSERT only if you prefer seeding instead of bootstrap:
-- Password hash must be generated with PasswordUtil.getHashpassword("ukdijs0987654321")

-- Customer example (normal email → /customer/home)
-- INSERT INTO users (username, email, password, phone, role, is_active)
-- VALUES ('Customer Demo', 'sthaujwal07@gmail.com', '<bcrypt-hash>', '9800000000', 'customer', TRUE);

-- Farmer / vendor example (@krishak.np → /farmer/dashboard)
-- INSERT INTO users (username, email, password, phone, role, is_active)
-- VALUES ('Farmer Demo', 'sthaujwal07@krishak.np', '<bcrypt-hash>', '9800000001', 'vendor', TRUE);
