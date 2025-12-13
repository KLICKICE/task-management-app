SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE tasks;
TRUNCATE TABLE task_priorities;
TRUNCATE TABLE task_statuses;
TRUNCATE TABLE projects;

SET FOREIGN_KEY_CHECKS = 1;


-- TASK STATUSES (локальні, НЕ project_statuses!)
DELETE FROM task_statuses;
ALTER TABLE task_statuses AUTO_INCREMENT = 1;

INSERT INTO task_statuses (id, status_task)
VALUES
    (1, 'NEW'),
    (2, 'IN_PROGRESS'),
    (3, 'DONE'),
    (4, 'CANCELLED');


-- TASK PRIORITIES
DELETE FROM task_priorities;
ALTER TABLE task_priorities AUTO_INCREMENT = 1;

INSERT INTO task_priorities (id, priority_status)
VALUES
    (1, 'LOW'),
    (2, 'MEDIUM'),
    (3, 'HIGH');


-- PROJECT
INSERT INTO projects (id, title, description, owner_id, status_id, start_date, end_date, created_at)
VALUES (1, 'Test Project', 'Description', 1, 1, NOW(), NOW(), NOW());


-- TASK
INSERT INTO tasks
(id, title, description, user_id, project_id, status_id, priority_id, deadline, created_at, updated_at)
VALUES
    (1, 'Test task', 'Test description', 1, 1, 1, 1, NOW(), NOW(), NOW());
