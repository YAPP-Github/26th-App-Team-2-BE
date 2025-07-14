CREATE TABLE group_app (
    group_app_id    bigint        NOT NULL AUTO_INCREMENT PRIMARY KEY,
    group_id        bigint        NOT NULL,
    app_id          VARCHAR(100)  NOT NULL,
    created_at      DATETIME      NOT NULL,
    updated_at      DATETIME      NOT NULL,
    UNIQUE KEY uk_group_app_group_id_app_id (group_id, app_id)
);

CREATE INDEX idx_group_app_group_id ON group_app (group_id);
