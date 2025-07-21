ALTER TABLE clients_tokens
    ADD deleted_by UUID;

ALTER TABLE clients_tokens
    ADD description VARCHAR(255);

ALTER TABLE clients_tokens
    ADD name VARCHAR(50);

ALTER TABLE clients_tokens
    ADD CONSTRAINT FK_CLIENTS_TOKENS_ON_DELETED_BY FOREIGN KEY (deleted_by) REFERENCES users (id);