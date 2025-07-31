CREATE TABLE app_settings
(
    id                        BIGINT                NOT NULL,
    track_opened_emails       BOOLEAN DEFAULT FALSE NOT NULL,
    allow_template_imports    BOOLEAN DEFAULT TRUE  NOT NULL,
    display_new_version_alert BOOLEAN DEFAULT TRUE  NOT NULL,
    use_gravatar              BOOLEAN DEFAULT TRUE  NOT NULL,
    updated_by                UUID,
    updated_on                TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_app_settings PRIMARY KEY (id)
);

ALTER TABLE app_settings
    ADD CONSTRAINT FK_APP_SETTINGS_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id);