ALTER TABLE templates
    ALTER COLUMN text_raw TYPE TEXT;

ALTER TABLE templates
    ALTER COLUMN html_raw TYPE TEXT;

ALTER TABLE templates
    ALTER COLUMN markdown_raw TYPE TEXT;

ALTER TABLE templates
    ALTER COLUMN schema TYPE TEXT;