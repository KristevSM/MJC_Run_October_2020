CREATE SCHEMA IF NOT EXISTS gift_certificate_service;

CREATE TABLE IF NOT EXISTS tag (
                     id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                     name varchar(40) NOT NULL UNIQUE,
                     PRIMARY KEY (id));

CREATE TABLE IF NOT EXISTS gift_certificate (
                                  id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  name varchar(40) NOT NULL,
                                  description varchar(250) NOT NULL,
                                  price DECIMAL(12,2) NOT NULL,
                                  create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  duration INT NOT NULL,
                                  PRIMARY KEY (id));


CREATE TABLE IF NOT EXISTS tag_has_gift_certificate (
                                          tag_id bigint(10) NOT NULL,
                                          gift_certificate_id bigint(10) NOT NULL,
                                          FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE RESTRICT ON UPDATE CASCADE,
                                          FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE RESTRICT ON UPDATE CASCADE,
                                          PRIMARY KEY (tag_id, gift_certificate_id)
);

CREATE TABLE IF NOT EXISTS users (
                                  user_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  first_name varchar(40) NOT NULL,
                                  last_name varchar(40) NOT NULL,
                                  email varchar(40) NOT NULL UNIQUE,
                                  password varchar(40) NOT NULL,
                                  address varchar(200) NOT NULL,
                                  date_of_birth DATE NOT NULL,
--                                   FOREIGN KEY (`user_id`) REFERENCES `Orders` (`order_id`),
                                  PRIMARY KEY (user_id));

CREATE TABLE IF NOT EXISTS orders (
                                  order_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  first_name varchar(40) NOT NULL,
                                  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (order_id) REFERENCES Users (user_id),
                                  PRIMARY KEY (order_id));

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
