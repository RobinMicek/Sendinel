ALTER TABLE templates
    ADD prefer_markdown BOOLEAN;

ALTER TABLE templates
    ADD reply_to VARCHAR(255);

ALTER TABLE templates
DROP
COLUMN prefered_template_type;

ALTER TABLE templates
DROP
COLUMN schema;

ALTER TABLE templates
    ADD schema VARCHAR(255);