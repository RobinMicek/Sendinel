ALTER TABLE emails
    ADD to_address VARCHAR(255);

ALTER TABLE emails
DROP
COLUMN "to";