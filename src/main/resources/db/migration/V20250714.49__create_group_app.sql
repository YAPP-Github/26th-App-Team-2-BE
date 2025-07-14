CREATE TABLE group_app (
    group_app_id    bigint        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    groupId         bigint        NOT NULL,
    appId           VARCHAR(100)  NOT NULL,
    created_at      DATETIME      NOT NULL
);

CREATE INDEX idx_group_app_group_id ON group_app (groupId);
