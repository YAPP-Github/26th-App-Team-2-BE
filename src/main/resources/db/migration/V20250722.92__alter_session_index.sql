ALTER TABLE session
DROP FOREIGN KEY session_ibfk_1,
DROP INDEX session_ibfk_1;

CREATE INDEX idx_session_member_id ON session (member_id);
