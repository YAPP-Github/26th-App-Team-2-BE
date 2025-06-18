CREATE TABLE `user` (
    user_id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    auth_email VARCHAR(255) NOT NULL,
    social_provider VARCHAR(50) NOT NULL,
    `role` VARCHAR(50) NOT NULL,
    deleted_at DATETIME NULL,
    `status` VARCHAR(50) NOT NULL,
    nickname VARCHAR(50) NULL,
    created_at DATETIME NULL,
    updated_at DATETIME NULL
);
