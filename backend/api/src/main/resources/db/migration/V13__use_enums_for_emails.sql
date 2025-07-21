ALTER TABLE emails
DROP
COLUMN priority;

ALTER TABLE emails
    ADD priority SMALLINT;

ALTER TABLE emails_statuses
DROP
COLUMN status;

ALTER TABLE emails_statuses
    ADD status SMALLINT;