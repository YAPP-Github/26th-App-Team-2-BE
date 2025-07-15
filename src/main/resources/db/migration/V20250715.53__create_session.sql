CREATE TABLE session
(
    session_id   bigint NOT NULL AUTO_INCREMENT PRIMARY KEY,
    group_id     bigint NOT NULL,
    member_id    bigint NOT NULL,
    date         DATE   NOT NULL,
    start_time   TIME   NOT NULL,
    end_time     TIME   NOT NULL,
    goal_time    bigint NOT NULL,
    snooze_count int    NOT NULL,
    snooze_unit  int    NOT NULL
);

