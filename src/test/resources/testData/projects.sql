DELETE FROM projects;
ALTER TABLE projects AUTO_INCREMENT = 1;

INSERT INTO projects (title, created_at, start_date, owner_id, description, end_date, status_id)
VALUES ('My First Project', NOW(), '2025-11-12', 1, 'This is a sample project description', '2025-12-31 23:59:59', 1);
