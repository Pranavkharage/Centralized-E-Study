-- Phase 4: profile fields. Hibernate (ddl-auto=update) adds these automatically on the first startup,
-- so running this file is OPTIONAL. It documents the schema change.
ALTER TABLE users
  ADD COLUMN first_name VARCHAR(50)  NULL,
  ADD COLUMN last_name  VARCHAR(50)  NULL,
  ADD COLUMN email      VARCHAR(100) NULL,
  ADD COLUMN phone      VARCHAR(20)  NULL,
  ADD COLUMN branch     VARCHAR(60)  NULL,
  ADD COLUMN study_year VARCHAR(30)  NULL,
  ADD UNIQUE KEY uk_users_email (email);
