CREATE TABLE daily_session_statistics
(
    date           date   NOT NULL,
    member_id      bigint NOT NULL,
    actual_minutes bigint NOT NULL,
    goal_minutes   bigint NOT NULL,
    PRIMARY KEY (date, member_id)
);

ALTER TABLE session
    CHANGE COLUMN goal_time goal_minutes bigint NOT NULL;

ALTER TABLE session
    CHANGE COLUMN start `start` DATETIME NOT NULL;

ALTER TABLE session
    CHANGE COLUMN end `end` DATETIME NOT NULL;
