CREATE TABLE device_profile
(
    device_profile_id bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id         bigint       NOT NULL,
    device_name       VARCHAR(255) NOT NULL,
    created_at        datetime     NULL,
    updated_at        datetime     NULL
);

ALTER TABLE `member`
    DROP INDEX uk_member_device_id;

ALTER TABLE `member`
    DROP COLUMN device_id;

ALTER TABLE `member`
    ADD UNIQUE KEY uk_member_auth_email_social_provider (auth_email, social_provider);

DROP INDEX idx_session_member_id ON session;
DROP INDEX idx_group_member_id ON `group`;

ALTER TABLE session
    CHANGE COLUMN member_id device_profile_id bigint NOT NULL;

ALTER TABLE `group`
    CHANGE COLUMN member_id device_profile_id bigint NOT NULL;

ALTER TABLE daily_session_statistic
    CHANGE COLUMN member_id device_profile_id bigint NOT NULL;

CREATE INDEX idx_session_device_profile_id ON session (device_profile_id);
CREATE INDEX idx_group_device_profile_id ON `group` (device_profile_id);

ALTER TABLE device_profile
    ADD UNIQUE KEY uk_device_profile_member_id_device_name (member_id, device_name);
