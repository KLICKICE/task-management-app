INSERT INTO tasks
(id, title, description, user_id, project_id, status, priority, deadline, created_at, updated_at)
VALUES
    (1, 'Test task', 'Test description', 1, 1, 'NEW', 'HIGH', NOW(), NOW(), NOW());
