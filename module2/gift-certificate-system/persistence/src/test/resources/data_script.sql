INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Certificate 1', 'Description of certificate 1', '23.50', '3');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Certificate 2', 'Description of certificate 2', '40', '6');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Certificate 3', 'Description of certificate 3', '65.5', '12');


INSERT INTO tag (name) VALUES ('Main');
INSERT INTO tag (name) VALUES ('Tag 2');
INSERT INTO tag (name) VALUES ('Tag 3');

INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('1', '1');
INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('2', '2');
INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('2', '3');
INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('3', '2');

INSERT INTO users (first_name, last_name, email, password, address, date_of_birth) VALUES ('Petr', 'Petrov', 'petrov@mail.com', 'qwerty', 'Address 1', '2000-10-12');
INSERT INTO users (first_name, last_name, email, password, address, date_of_birth) VALUES ('Ivan', 'Ivanov', 'ivanov@mail.com', 'qwerty', 'Address 2', '1990-09-17');
