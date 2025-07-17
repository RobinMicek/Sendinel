ALTER TABLE templates
    ADD html_raw TEXT;

ALTER TABLE templates
    ADD markdown_raw TEXT;

ALTER TABLE templates
    ADD prefered_template_type SMALLINT;

ALTER TABLE templates
    ADD text_raw TEXT;

ALTER TABLE templates
DROP
COLUMN template;