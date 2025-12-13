DELETE FROM users_roles;
DELETE FROM users;

INSERT INTO users (id, username, email, password, first_name, last_name)
VALUES (1, 'user@example.com', 'user@example.com',
        '$2a$10$pXf9YEACrJHBk17Q/FEXdO.txASVXQkllq5hMestMMqj0sp76Moh.',
        'John', 'Doe');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
