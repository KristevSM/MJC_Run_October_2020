CREATE TABLE tag (
  id bigint(10) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);

CREATE TABLE gift_certificate (
  id bigint(10) NOT NULL AUTO_INCREMENT,
  name varchar(40) NOT NULL,
  description varchar(250) NOT NULL,
  price DECIMAL(12,2) NOT NULL,
  create_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_update_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  duration INT NOT NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);


CREATE TABLE tag_has_gift_certificate (
  tag_id bigint(10) NOT NULL,
  gift_certificate_id bigint(10) NOT NULL,
  FOREIGN KEY (tag_id) REFERENCES tag (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (gift_certificate_id) REFERENCES gift_certificate (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  PRIMARY KEY (tag_id, gift_certificate_id)
);