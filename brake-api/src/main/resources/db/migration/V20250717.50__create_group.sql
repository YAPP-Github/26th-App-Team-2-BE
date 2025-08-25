CREATE TABLE `group` (
    group_id        bigint        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    member_id       bigint        NOT NULL,
    `name`          VARCHAR(100)  NOT NULL,
    created_at      DATETIME      NOT NULL,
    updated_at      DATETIME      NOT NULL
);

ALTER TABLE group_app
DROP INDEX uk_group_app_group_id_app_id;

ALTER TABLE group_app
CHANGE COLUMN app_id `name` VARCHAR(100) NOT NULL;

ALTER TABLE group_app
ADD UNIQUE KEY uk_group_app_group_id_name (group_id, name);
