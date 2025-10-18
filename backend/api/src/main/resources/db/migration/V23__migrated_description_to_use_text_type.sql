ALTER TABLE clients_tokens
ALTER
COLUMN description TYPE TEXT USING (description::TEXT);

ALTER TABLE senders
ALTER
COLUMN description TYPE TEXT USING (description::TEXT);