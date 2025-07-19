ALTER TABLE senders
DROP
COLUMN type;

ALTER TABLE senders
    ADD type SMALLINT;