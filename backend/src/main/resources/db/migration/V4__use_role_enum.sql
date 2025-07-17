ALTER TABLE users
    ADD created_by UUID;

ALTER TABLE users
    ADD updated_by UUID;

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id);

ALTER TABLE users
DROP
COLUMN role;

ALTER TABLE users
    ADD role SMALLINT;