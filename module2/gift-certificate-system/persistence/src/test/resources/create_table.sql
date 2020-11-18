
CREATE TABLE tag (
                     id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                     name varchar(40) NOT NULL UNIQUE,
                     PRIMARY KEY (id));

CREATE TABLE gift_certificate (
                                  id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  name varchar(40) NOT NULL UNIQUE,
                                  description varchar(500) NOT NULL,
                                  price DECIMAL(12,2) NOT NULL,
                                  create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  duration INT NOT NULL,
                                  PRIMARY KEY (id));



CREATE TABLE users (
                                  user_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  first_name varchar(40) NOT NULL,
                                  last_name varchar(40) NOT NULL,
                                  email varchar(40) NOT NULL UNIQUE,
                                  password varchar(40) NOT NULL,
                                  address varchar(200) NOT NULL,
                                  date_of_birth DATE NOT NULL,
--                                   FOREIGN KEY (`user_id`) REFERENCES `Orders` (`order_id`),
                                  PRIMARY KEY (user_id));

CREATE TABLE orders (
                                  order_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (`order_id`) REFERENCES `Users` (`user_id`),
                                  PRIMARY KEY (order_id));
