SET FOREIGN_KEY_CHECKS = 0;

-- Очищення залежних таблиць
TRUNCATE TABLE comments;
TRUNCATE TABLE tasks;

-- Далі таблиці, від яких залежать інші
TRUNCATE TABLE projects;

-- Join-таблиці
TRUNCATE TABLE users_roles;

-- Основні таблиці
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

-- Скидання AUTO_INCREMENT
ALTER TABLE comments AUTO_INCREMENT = 1;
ALTER TABLE tasks AUTO_INCREMENT = 1;
ALTER TABLE projects AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE roles AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;
