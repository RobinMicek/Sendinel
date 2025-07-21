ALTER TABLE users_totp
    ADD activated BOOLEAN DEFAULT FALSE;

ALTER TABLE users_totp
    ALTER COLUMN activated SET NOT NULL;

ALTER TABLE users_totp
DROP
COLUMN deleted_on;