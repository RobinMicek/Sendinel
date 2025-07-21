ALTER TABLE senders
DROP
COLUMN configuration;

ALTER TABLE senders
DROP
COLUMN type;

ALTER TABLE senders
    ADD configuration JSONB;

ALTER TABLE senders
    ADD type SMALLINT;