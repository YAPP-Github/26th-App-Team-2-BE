CREATE TABLE outbox (
    outbox_id   CHAR(36)     NOT NULL PRIMARY KEY,
    handler  VARCHAR(100)  NOT NULL,
    event_type  VARCHAR(100)  NOT NULL,
    payload     VARCHAR(5000) NOT NULL,
    created_at  DATETIME      NOT NULL
);

CREATE INDEX idx_outbox_created_at ON outbox (created_at);

ALTER TABLE `member`
ADD CONSTRAINT uk_member_device_id UNIQUE (`device_id`);
