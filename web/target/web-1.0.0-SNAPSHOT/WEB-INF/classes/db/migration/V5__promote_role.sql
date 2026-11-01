UPDATE users
SET role = 'ROLE_ADMIN', verified = TRUE
WHERE email = 'admin@system.local';

UPDATE users
SET role = 'ROLE_USER', verified = TRUE
WHERE email = 'john_doe@system.local';