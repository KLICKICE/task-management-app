DELETE FROM projects;

INSERT INTO projects (id, title, created_at, start_date, owner_id, description, end_date, status)
VALUES (1, 'My First Project', NOW(), '2025-11-12', 1,
        'This is a sample project description', '2025-12-31', 'INITIATED');
