DELETE FROM roles;
ALTER TABLE roles AUTO_INCREMENT = 1;

INSERT INTO roles (id, role_name)
VALUES (1, 'USER'),
       (2, 'ADMIN');
