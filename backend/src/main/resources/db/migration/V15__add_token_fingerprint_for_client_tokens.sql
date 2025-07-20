ALTER TABLE clients_tokens
    ADD token_fingerprint VARCHAR(128);

ALTER TABLE clients_tokens
    ALTER COLUMN token_fingerprint SET NOT NULL;

ALTER TABLE clients_tokens
    ALTER COLUMN token_hash SET NOT NULL;