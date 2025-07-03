ALTER TABLE `member`
ADD COLUMN device_id VARCHAR(255) NOT NULL AFTER member_id;

ALTER TABLE `member`
ADD CONSTRAINT uq_member_device_id UNIQUE (device_id);

ALTER TABLE `member`
ADD COLUMN oauth_id VARCHAR(255) NOT NULL AFTER auth_email;

ALTER TABLE `member`
DROP COLUMN deleted_at;
