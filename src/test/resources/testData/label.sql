DELETE FROM labels;

ALTER TABLE labels AUTO_INCREMENT = 1;

INSERT INTO labels (id, name, color)
VALUES (1, 'Important', 'black');

INSERT INTO labels (id, name, color)
VALUES (2, 'Bug', 'white');

INSERT INTO labels (id, name, color)
VALUES (3, 'Feature', 'grey');
