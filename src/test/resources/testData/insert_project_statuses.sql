INSERT INTO project_statuses (id, status_project)
SELECT 1, 'INITIATED' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM project_statuses WHERE id = 1);

INSERT INTO project_statuses (id, status_project)
SELECT 2, 'IN_PROGRESS' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM project_statuses WHERE id = 2);

INSERT INTO project_statuses (id, status_project)
SELECT 3, 'COMPLETED' FROM dual
WHERE NOT EXISTS (SELECT 1 FROM project_statuses WHERE id = 3);
