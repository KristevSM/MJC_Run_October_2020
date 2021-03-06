-- MySQL Script generated by MySQL Workbench
-- Wed Nov 11 01:23:14 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema gift_certificate_service
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gift_certificate_service
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gift_certificate_service` DEFAULT CHARACTER SET utf8 ;
USE `gift_certificate_service` ;

-- -----------------------------------------------------
-- Table `gift_certificate_service`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_service`.`tag` (
  `tag_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tag_id`),
  UNIQUE INDEX `tag_id_UNIQUE` (`tag_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate_service`.`gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_service`.`gift_certificate` (
  `certificate_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  `price` DECIMAL(12,2) NOT NULL,
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `duration` INT NOT NULL,
  PRIMARY KEY (`certificate_id`),
  UNIQUE INDEX `gift_sertificate_id_UNIQUE` (`certificate_id` ASC) VISIBLE,
  UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate_service`.`tag_has_gift_certificate`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_service`.`tag_has_gift_certificate` (
  `tag_id` BIGINT(10) NOT NULL,
  `gift_certificate_id` BIGINT(10) NOT NULL,
  PRIMARY KEY (`tag_id`, `gift_certificate_id`),
  INDEX `fk_tag_has_gift_sertificate_gift_sertificate1_idx` (`gift_certificate_id` ASC) VISIBLE,
  INDEX `fk_tag_has_gift_sertificate_tag_idx` (`tag_id` ASC) VISIBLE,
  CONSTRAINT `fk_tag_has_gift_sertificate_tag`
    FOREIGN KEY (`tag_id`)
    REFERENCES `gift_certificate_service`.`tag` (`tag_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_tag_has_gift_sertificate_gift_sertificate1`
    FOREIGN KEY (`gift_certificate_id`)
    REFERENCES `gift_certificate_service`.`gift_certificate` (`certificate_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate_service`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_service`.`orders` (
  `order_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `certificate_id` BIGINT(10),
  `cost` DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (`order_id`),
  UNIQUE INDEX `order_id_UNIQUE` (`order_id` ASC) VISIBLE,
  INDEX `fk_orders_gift_certificate1_idx` (`certificate_id` ASC) VISIBLE,
  CONSTRAINT `fk_orders_gift_certificate1`
    FOREIGN KEY (`certificate_id`)
    REFERENCES `gift_certificate_service`.`gift_certificate` (`certificate_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `gift_certificate_service`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gift_certificate_service`.`users` (
  `user_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(40) NOT NULL,
  `last_name` VARCHAR(40) NOT NULL,
  `email` VARCHAR(40) NOT NULL,
  `password` VARCHAR(40) NOT NULL,
  `address` VARCHAR(200) NOT NULL,
  `date_of_birth` DATE NOT NULL,
  `order_id` BIGINT(10) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `user_id_UNIQUE` (`user_id` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  INDEX `fk_users_orders1_idx` (`order_id` ASC) VISIBLE,
  CONSTRAINT `fk_users_orders1`
    FOREIGN KEY (`order_id`)
    REFERENCES `gift_certificate_service`.`orders` (`order_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
