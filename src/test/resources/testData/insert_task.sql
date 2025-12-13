-- (якщо у тебе clean.sql не чистить ці таблиці — додай DELETE тут)
DELETE FROM tasks;
DELETE FROM projects;
DELETE FROM task_priorities;
DELETE FROM task_statuses;

-- важливо: project_statuses для status_id=1
DELETE FROM project_statuses;
INSERT INTO project_statuses (id, status_project) VALUES (1, 'INITIATED');
INSERT INTO project_statuses (id, status_project) VALUES (2, 'IN_PROGRESS');
INSERT INTO project_statuses (id, status_project) VALUES (3, 'COMPLETED');

-- task statuses
INSERT INTO task_statuses (id, status_task) VALUES (1, 'NEW');
INSERT INTO task_statuses (id, status_task) VALUES (2, 'IN_PROGRESS');
INSERT INTO task_statuses (id, status_task) VALUES (3, 'DONE');
INSERT INTO task_statuses (id, status_task) VALUES (4, 'CANCELLED');

-- priorities
INSERT INTO task_priorities (id, priority_status) VALUES (1, 'LOW');
INSERT INTO task_priorities (id, priority_status) VALUES (2, 'MEDIUM');
INSERT INTO task_priorities (id, priority_status) VALUES (3, 'HIGH');

-- project (owner_id=1 існує з insert_users.sql, status_id=1 існує вище)
INSERT INTO projects (id, title, description, owner_id, status_id, start_date, end_date, created_at)
VALUES (1, 'Test Project', 'Description', 1, 1, CURRENT_DATE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- task
INSERT INTO tasks (id, title, description, user_id, project_id, status_id, priority_id, deadline, created_at, updated_at)
VALUES (1, 'Test task', 'Test description', 1, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
