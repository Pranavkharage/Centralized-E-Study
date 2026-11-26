-- Run against the existing Aiven MySQL database after taking a backup.
-- If the role column already exists, skip the ALTER TABLE statement.
ALTER TABLE users
    ADD COLUMN role VARCHAR(20) NOT NULL DEFAULT 'STUDENT';

-- After creating/logging in to the account that should administer the platform,
-- replace the username below with that exact verified username and run this update:
-- UPDATE users SET role = 'ADMIN' WHERE username = 'your_admin_username';
