ALTER TABLE templates
DROP
COLUMN schema;

ALTER TABLE templates
    ADD schema JSONB;