SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE attachments;
TRUNCATE TABLE comments;
TRUNCATE TABLE task_labels;
TRUNCATE TABLE tasks;

TRUNCATE TABLE labels;
TRUNCATE TABLE task_priorities;
TRUNCATE TABLE task_statuses;

TRUNCATE TABLE projects;
TRUNCATE TABLE project_statuses;

TRUNCATE TABLE users_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

SET FOREIGN_KEY_CHECKS = 1;
