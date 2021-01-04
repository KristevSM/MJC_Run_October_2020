CREATE SCHEMA IF NOT EXISTS gift_certificate_service;

CREATE TABLE IF NOT EXISTS tag (
                     tag_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                     name varchar(40) NOT NULL UNIQUE,
                     PRIMARY KEY (tag_id));

CREATE TABLE IF NOT EXISTS gift_certificate (
                                  certificate_id bigint(10) NOT NULL AUTO_INCREMENT UNIQUE,
                                  name varchar(40) NOT NULL UNIQUE,
                                  description varchar(500) NOT NULL,
                                  price DECIMAL(12,2) NOT NULL,
                                  create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  duration INT NOT NULL,
                                  PRIMARY KEY (certificate_id));


CREATE TABLE IF NOT EXISTS tag_has_gift_certificate (
                                          tag_id bigint(10) NOT NULL,
                                          gift_certificate_id bigint(10) NOT NULL,
                                          FOREIGN KEY (tag_id) REFERENCES tag (tag_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                                          FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (certificate_id) ON DELETE RESTRICT ON UPDATE CASCADE,
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
                                  order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--                                   FOREIGN KEY (order_id) REFERENCES Users (user_id),
                                  PRIMARY KEY (order_id));

INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Apple Gift Card $25', 'Use the Apple Gift Card to get products, accessories, apps, games, music, movies, TV shows, and more. Spend it on in-app content, books, subscriptions and even iCloud storage to secure files from all your Apple devices. This gift card does it all. And then some.', '25', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Apple Gift Card $50', 'Use the Apple Gift Card to get products, accessories, apps, games, music, movies, TV shows, and more. Spend it on in-app content, books, subscriptions and even iCloud storage to secure files from all your Apple devices. This gift card does it all. And then some.', '50', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Apple Gift Card $100', 'Use the Apple Gift Card to get products, accessories, apps, games, music, movies, TV shows, and more. Spend it on in-app content, books, subscriptions and even iCloud storage to secure files from all your Apple devices. This gift card does it all. And then some.', '100', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Domino’s® Pizza Gift Card $20', 'Domino''s® is more than pizza! Try our mouth-watering Bread Twists, Oven Baked Sandwiches, Pastas, Chicken, pizza & more. Order online at www.dominos.com for lunch, dinner or for your next event. No expiration date and no service fees. Can be redeemed at nearly 5,500 stores in the United States. For full details, visit www.dominos.com.', '20', '120');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Domino’s® Pizza Gift Card $30', 'Domino''s® is more than pizza! Try our mouth-watering Bread Twists, Oven Baked Sandwiches, Pastas, Chicken, pizza & more. Order online at www.dominos.com for lunch, dinner or for your next event. No expiration date and no service fees. Can be redeemed at nearly 5,500 stores in the United States. For full details, visit www.dominos.com.', '30', '120');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Domino’s® Pizza Gift Card $40', 'Domino''s® is more than pizza! Try our mouth-watering Bread Twists, Oven Baked Sandwiches, Pastas, Chicken, pizza & more. Order online at www.dominos.com for lunch, dinner or for your next event. No expiration date and no service fees. Can be redeemed at nearly 5,500 stores in the United States. For full details, visit www.dominos.com.', '40', '120');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Google Play Gift Code $25', 'Power up in over 1M Android apps and games on Google Play, the world''s largest mobile gaming platform. Use a Google Play gift code to go further in your favorite games like Clash Royale or Pokemon Go or redeem your code for the latest apps, movies, music, books, and more. There’s no credit card required, and balances never expire. Treat yourself or give the gift of Play today.', '25', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Google Play Gift Code $50', 'Power up in over 1M Android apps and games on Google Play, the world''s largest mobile gaming platform. Use a Google Play gift code to go further in your favorite games like Clash Royale or Pokemon Go or redeem your code for the latest apps, movies, music, books, and more. There’s no credit card required, and balances never expire. Treat yourself or give the gift of Play today.', '50', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Google Play Gift Code $100', 'Power up in over 1M Android apps and games on Google Play, the world''s largest mobile gaming platform. Use a Google Play gift code to go further in your favorite games like Clash Royale or Pokemon Go or redeem your code for the latest apps, movies, music, books, and more. There’s no credit card required, and balances never expire. Treat yourself or give the gift of Play today.', '100', '365');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Target GiftCard™ $25', 'A Target GiftCard™ is your opportunity to shop for thousands of items at more than 1,800 Target stores in the U.S. and online at Target.com. From toys and electronics to clothing and housewares, find exactly what you''re looking for at Target. No fees. No expiration. No kidding.', '25', '30');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Target GiftCard™ $50', 'A Target GiftCard™ is your opportunity to shop for thousands of items at more than 1,800 Target stores in the U.S. and online at Target.com. From toys and electronics to clothing and housewares, find exactly what you''re looking for at Target. No fees. No expiration. No kidding.', '50', '30');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('Target GiftCard™ $100', 'A Target GiftCard™ is your opportunity to shop for thousands of items at more than 1,800 Target stores in the U.S. and online at Target.com. From toys and electronics to clothing and housewares, find exactly what you''re looking for at Target. No fees. No expiration. No kidding.', '100', '30');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('The Home Depot® eGift Card $25', 'Whether remodeling your kitchen or purchasing new appliances and power tools, The Home Depot® provides products and services for all your home improvement needs.', '25', '60');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('The Home Depot® eGift Card $50', 'Whether remodeling your kitchen or purchasing new appliances and power tools, The Home Depot® provides products and services for all your home improvement needs.', '50', '60');
INSERT INTO gift_certificate (name, description, price, duration) VALUES ('The Home Depot® eGift Card $100', 'Whether remodeling your kitchen or purchasing new appliances and power tools, The Home Depot® provides products and services for all your home improvement needs.', '100', '60');


INSERT INTO tag (name) VALUES ('Apple');
INSERT INTO tag (name) VALUES ('Dominos');
INSERT INTO tag (name) VALUES ('Google Play');
INSERT INTO tag (name) VALUES ('Toys');
INSERT INTO tag (name) VALUES ('The Home Depot');
INSERT INTO tag (name) VALUES ('Electronics');
INSERT INTO tag (name) VALUES ('App');
INSERT INTO tag (name) VALUES ('Music');
INSERT INTO tag (name) VALUES ('Pizza');
INSERT INTO tag (name) VALUES ('Food');
INSERT INTO tag (name) VALUES ('Games');
INSERT INTO tag (name) VALUES ('Home');
--
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('1', '1');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('6', '1');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '1');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('8', '1');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('1', '2');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('6', '2');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '2');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('8', '2');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('1', '3');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('6', '3');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '3');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('8', '3');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('9', '4');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('10', '4');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('9', '5');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('10', '5');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('9', '6');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('10', '6');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('3', '7');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '7');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('3', '8');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '8');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('3', '9');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('7', '9');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('4', '10');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '10');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('4', '11');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '11');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('4', '12');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '12');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '13');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '14');
-- INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) VALUES ('12', '15');

INSERT INTO users (first_name, last_name, email, password, address, date_of_birth) VALUES ('Petr', 'Petrov', 'petrov@mail.com', 'qwerty', 'Address 1', '2000-10-12');
INSERT INTO users (first_name, last_name, email, password, address, date_of_birth) VALUES ('Ivan', 'Ivanov', 'ivanov@mail.com', 'qwerty', 'Address 2', '1990-09-17');
