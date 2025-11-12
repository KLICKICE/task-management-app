DELETE FROM users_roles;
DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;
ALTER TABLE users_roles AUTO_INCREMENT = 1;

INSERT INTO users (username, email, password, first_name, last_name)
VALUES ('user', 'user@example.com', '$2a$10$pXf9YEACrJHBk17Q/FEXdO.txASVXQkllq5hMestMMqj0sp76Moh.', 'John', 'Doe');

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1);
