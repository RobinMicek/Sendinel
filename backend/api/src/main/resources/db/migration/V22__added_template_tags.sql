CREATE TABLE templates_tags
(
    id         UUID         NOT NULL,
    name       VARCHAR(255) NOT NULL,
    created_by UUID,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_templates_tags PRIMARY KEY (id)
);

CREATE TABLE templates_tags_rel
(
    tag      UUID NOT NULL,
    template UUID NOT NULL,
    CONSTRAINT pk_templates_tags_rel PRIMARY KEY (tag, template)
);

ALTER TABLE templates_tags
    ADD CONSTRAINT FK_TEMPLATES_TAGS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE templates_tags_rel
    ADD CONSTRAINT fk_temtagrel_on_template FOREIGN KEY (template) REFERENCES templates (id);

ALTER TABLE templates_tags_rel
    ADD CONSTRAINT fk_temtagrel_on_template_tag FOREIGN KEY (tag) REFERENCES templates_tags (id);