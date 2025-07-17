CREATE TABLE clients
(
    id          UUID                                      NOT NULL,
    name        VARCHAR(50),
    description TEXT,
    sender_id   UUID,
    created_by  UUID,
    updated_by  UUID,
    created_on  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_on  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    deleted_on  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_clients PRIMARY KEY (id)
);

CREATE TABLE clients_tokens
(
    id         UUID                                      NOT NULL,
    client_id  UUID                                      NOT NULL,
    token_hash VARCHAR(128),
    created_by UUID,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    deleted_on TIMESTAMP WITHOUT TIME ZONE,
    expiration TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_clients_tokens PRIMARY KEY (id)
);

CREATE TABLE emails
(
    id                 UUID                                      NOT NULL,
    "to"               VARCHAR(255),
    track_code         VARCHAR(255),
    template           UUID                                      NOT NULL,
    template_variables JSONB,
    priority           VARCHAR(25),
    sent_by            UUID,
    requested_by       UUID,
    created_on         TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_emails PRIMARY KEY (id)
);

CREATE TABLE emails_statuses
(
    id         UUID                                      NOT NULL,
    email_id   UUID                                      NOT NULL,
    status     VARCHAR(25),
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_emails_statuses PRIMARY KEY (id)
);

CREATE TABLE senders
(
    id            UUID                                      NOT NULL,
    name          VARCHAR(50),
    type          VARCHAR(25),
    description   VARCHAR(255),
    configuration TEXT,
    created_by    UUID,
    updated_by    UUID,
    created_on    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_on    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    deleted_on    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_senders PRIMARY KEY (id)
);

CREATE TABLE templates
(
    id          UUID                                      NOT NULL,
    name        VARCHAR(255),
    description TEXT,
    subject     VARCHAR(255),
    template    TEXT,
    schema      JSONB,
    created_by  UUID,
    updated_by  UUID,
    created_on  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_on  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    deleted_on  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_templates PRIMARY KEY (id)
);

CREATE TABLE users
(
    id            UUID                                      NOT NULL,
    firstname     VARCHAR(25),
    lastname      VARCHAR(25),
    email         VARCHAR(255),
    password_hash VARCHAR(128),
    role          VARCHAR(25),
    created_on    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_on    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    deleted_on    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_totp
(
    id         UUID                                      NOT NULL,
    user_id    UUID                                      NOT NULL,
    secret     TEXT,
    created_on TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_users_totp PRIMARY KEY (id)
);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_SENDER FOREIGN KEY (sender_id) REFERENCES senders (id);

ALTER TABLE clients
    ADD CONSTRAINT FK_CLIENTS_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id);

ALTER TABLE clients_tokens
    ADD CONSTRAINT FK_CLIENTS_TOKENS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES clients (id);

ALTER TABLE clients_tokens
    ADD CONSTRAINT FK_CLIENTS_TOKENS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE emails
    ADD CONSTRAINT FK_EMAILS_ON_REQUESTED_BY FOREIGN KEY (requested_by) REFERENCES clients (id);

ALTER TABLE emails
    ADD CONSTRAINT FK_EMAILS_ON_SENT_BY FOREIGN KEY (sent_by) REFERENCES senders (id);

ALTER TABLE emails
    ADD CONSTRAINT FK_EMAILS_ON_TEMPLATE FOREIGN KEY (template) REFERENCES templates (id);

ALTER TABLE emails_statuses
    ADD CONSTRAINT FK_EMAILS_STATUSES_ON_EMAIL FOREIGN KEY (email_id) REFERENCES emails (id);

ALTER TABLE senders
    ADD CONSTRAINT FK_SENDERS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE senders
    ADD CONSTRAINT FK_SENDERS_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id);

ALTER TABLE templates
    ADD CONSTRAINT FK_TEMPLATES_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE templates
    ADD CONSTRAINT FK_TEMPLATES_ON_UPDATED_BY FOREIGN KEY (updated_by) REFERENCES users (id);

ALTER TABLE users_totp
    ADD CONSTRAINT FK_USERS_TOTP_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);