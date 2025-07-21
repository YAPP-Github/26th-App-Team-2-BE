SELECT constraint_name
INTO @fk_name
FROM information_schema.table_constraints
WHERE table_schema = DATABASE()
  AND table_name = 'session'
  AND constraint_type = 'FOREIGN KEY'
    LIMIT 1;

SET @sql = CONCAT(
  'ALTER TABLE `session` DROP FOREIGN KEY `',
  @fk_name,
  '`;'
);

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

CREATE INDEX idx_session_member_id ON session (member_id);
