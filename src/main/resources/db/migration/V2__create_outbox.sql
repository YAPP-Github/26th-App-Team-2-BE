CREATE TABLE outbox (
    outbox_id   BIGINT        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    event_type  VARCHAR(100)  NOT NULL,
    payload     VARCHAR(5000) NOT NULL,
    created_at  DATETIME      NOT NULL
);

ALTER TABLE `member`
ADD CONSTRAINT uk_member_device_id UNIQUE (`device_id`);
