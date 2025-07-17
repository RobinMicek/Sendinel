ALTER TABLE users_totp
    ADD deleted_on TIMESTAMP WITHOUT TIME ZONE;

ALTER TABLE users_totp
    ADD updated_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW();

ALTER TABLE users_totp
    ALTER COLUMN updated_on SET NOT NULL;

ALTER TABLE users_totp
    ADD CONSTRAINT uc_users_totp_user UNIQUE (user_id);