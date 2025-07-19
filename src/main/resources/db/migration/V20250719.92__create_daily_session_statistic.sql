CREATE TABLE daily_session_statistics
(
    date           date   NOT NULL,
    member_id      bigint NOT NULL,
    actual_minutes bigint NOT NULL,
    goal_minutes   bigint NOT NULL,
    PRIMARY KEY (date, member_id)
);
