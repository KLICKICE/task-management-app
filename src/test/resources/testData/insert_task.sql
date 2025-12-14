-- TASK STATUSES
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

-- TASK (project_id=1 має існувати, якщо тест його використовує)
INSERT INTO tasks
(id, title, description, user_id, project_id, status_id, priority_id, deadline, created_at, updated_at)
VALUES
    (1, 'Test task', 'Test description', 1, 1, 1, 1, NOW(), NOW(), NOW());
