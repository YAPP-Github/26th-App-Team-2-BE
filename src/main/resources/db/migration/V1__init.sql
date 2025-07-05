CREATE TABLE `member`
(
    member_id       bigint       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    device_id      VARCHAR(255) NOT NULL,
    auth_email      VARCHAR(255) NOT NULL,
    auth_id      VARCHAR(255) NOT NULL,
    social_provider VARCHAR(50)  NOT NULL,
    `role`          VARCHAR(50)  NOT NULL,
    `state`        VARCHAR(50)  NOT NULL,
    nickname        VARCHAR(50) NULL,
    created_at      datetime NULL,
    updated_at      datetime NULL
);
