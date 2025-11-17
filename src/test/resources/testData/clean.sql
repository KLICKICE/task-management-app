-- Вимикаємо перевірку зовнішніх ключів, щоб можна було очищати таблиці
SET FOREIGN_KEY_CHECKS = 0;

-- Очищуємо таблиці в правильному порядку (спочатку залежні)
TRUNCATE TABLE comments;
TRUNCATE TABLE users_roles;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;
TRUNCATE TABLE projects;
TRUNCATE TABLE tasks;
TRUNCATE TABLE project_statuses;

-- Скидаємо AUTO_INCREMENT для всіх таблиць
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE roles AUTO_INCREMENT = 1;
ALTER TABLE users_roles AUTO_INCREMENT = 1;
ALTER TABLE projects AUTO_INCREMENT = 1;
ALTER TABLE tasks AUTO_INCREMENT = 1;
ALTER TABLE comments AUTO_INCREMENT = 1;
ALTER TABLE project_statuses AUTO_INCREMENT = 1;

-- Вмикаємо перевірку зовнішніх ключів назад
SET FOREIGN_KEY_CHECKS = 1;
