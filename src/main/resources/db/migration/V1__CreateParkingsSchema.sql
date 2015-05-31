SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema get_my_parking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `get_my_parking` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `get_my_parking` ;

-- -----------------------------------------------------
-- Table `get_my_parking`.`company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`company` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`company` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NOT NULL,
  `address` VARCHAR(500) NOT NULL,
  `city` VARCHAR(500) NOT NULL,
  `email` VARCHAR(500) NULL,
  `contact_number` VARCHAR(500) NULL,
  `website` VARCHAR(500) NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  `contact_number` VARCHAR(45) NULL,
  `company_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_company1_idx` (`company_id` ASC),
  CONSTRAINT `fk_parking_company1`
  FOREIGN KEY (`company_id`)
  REFERENCES `get_my_parking`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_lot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking_lot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_lot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `open_time` TIME NOT NULL,
  `close_time` TIME NOT NULL,
  `latitude` DECIMAL(10,10) NOT NULL,
  `longitude` DECIMAL(10,10) NOT NULL,
  `car_capacity` INT NOT NULL,
  `bike_capacity` INT NOT NULL,
  `license_no` VARCHAR(500) NULL,
  `parking_id` INT NOT NULL,
  `collection_model` VARCHAR(500) NULL COMMENT 'PREPAID / POSTPAID / HYBRID',
  PRIMARY KEY (`id`),
  INDEX `fk_parking_lot_parking1_idx` (`parking_id` ASC),
  CONSTRAINT `fk_parking_lot_parking1`
  FOREIGN KEY (`parking_id`)
  REFERENCES `get_my_parking`.`parking` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_pass_master`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking_pass_master` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_pass_master` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NOT NULL,
  `pass_type` VARCHAR(45) NOT NULL COMMENT 'Weekly / Monthly / Daily / Hourly',
  `numbers` INT NOT NULL,
  `price` INT NOT NULL,
  `parking_lot_id` INT NOT NULL,
  `vehicle_type` VARCHAR(45) NOT NULL,
  `isActive` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_pass_parking_lot1_idx` (`parking_lot_id` ASC),
  CONSTRAINT `fk_parking_pass_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_pass`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking_pass` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_pass` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `registration_number` VARCHAR(500) NOT NULL,
  `valid_time` TIMESTAMP NOT NULL,
  `parking_pass_master_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_pass_parking_pass_master1_idx` (`parking_pass_master_id` ASC),
  CONSTRAINT `fk_parking_pass_parking_pass_master1`
  FOREIGN KEY (`parking_pass_master_id`)
  REFERENCES `get_my_parking`.`parking_pass_master` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking_event` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `vehicle_type` VARCHAR(45) NOT NULL,
  `type` VARCHAR(45) NOT NULL COMMENT 'Standard / Pass',
  `registration_number` VARCHAR(45) NOT NULL,
  `event_time` TIMESTAMP NOT NULL,
  `event_type` VARCHAR(500) NOT NULL,
  `cost` DECIMAL(10,2) NOT NULL,
  `parking_lot_id` INT NOT NULL,
  `parking_pass_id` INT NULL,
  `serial_number` VARCHAR(500) NOT NULL,
  `shift_number` VARCHAR(500) NOT NULL,
  `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_parkings_parking_lot1_idx` (`parking_lot_id` ASC),
  INDEX `fk_parkings_parking_pass1_idx` (`parking_pass_id` ASC),
  CONSTRAINT `fk_parkings_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parkings_parking_pass1`
  FOREIGN KEY (`parking_pass_id`)
  REFERENCES `get_my_parking`.`parking_pass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`pricing_slot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`pricing_slot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`pricing_slot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `vehicle_type` VARCHAR(255) NULL COMMENT 'BIKE / CAR',
  `day` VARCHAR(45) NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  `parking_lot_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pricing_parking_lot1_idx` (`parking_lot_id` ASC),
  CONSTRAINT `fk_pricing_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`price_grid`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`price_grid` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`price_grid` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price_structure` VARCHAR(45) NOT NULL COMMENT 'INCREMENTAL / FLAT',
  `start_hour` INT NULL,
  `end_hour` INT NULL,
  `cost` INT NOT NULL,
  `slab_hour` VARCHAR(45) NULL,
  `pricing_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_price_grid_parking_lot_price1_idx` (`pricing_id` ASC),
  CONSTRAINT `fk_price_grid_parking_lot_price1`
  FOREIGN KEY (`pricing_id`)
  REFERENCES `get_my_parking`.`pricing_slot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`user_b2b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`user_b2b` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`user_b2b` (
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(1000) NOT NULL,
  `name` VARCHAR(255) NULL,
  `contact_number` VARCHAR(255) NULL,
  `role` VARCHAR(255) NULL,
  PRIMARY KEY (`username`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_lot_has_user_b2b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`parking_lot_has_user_b2b` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_lot_has_user_b2b` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `parking_lot_id` INT NOT NULL,
  `user_b2b_username` VARCHAR(255) NOT NULL,
  INDEX `fk_parking_lot_has_user_b2b_user_b2b1_idx` (`user_b2b_username` ASC),
  INDEX `fk_parking_lot_has_user_b2b_parking_lot1_idx` (`parking_lot_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_parking_lot_has_user_b2b_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parking_lot_has_user_b2b_user_b2b1`
  FOREIGN KEY (`user_b2b_username`)
  REFERENCES `get_my_parking`.`user_b2b` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`session` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`session` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `auth_token` VARCHAR(1000) NOT NULL,
  `valid_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_session_user_b2b1_idx` (`username` ASC),
  CONSTRAINT `fk_session_user_b2b1`
  FOREIGN KEY (`username`)
  REFERENCES `get_my_parking`.`user_b2b` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`receipt_content`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking`.`receipt_content` ;

CREATE TABLE IF NOT EXISTS `get_my_parking`.`receipt_content` (
  `id` INT NOT NULL,
  `content` VARCHAR(500) NULL,
  `ordering` VARCHAR(45) NULL,
  `placement` VARCHAR(45) NULL,
  `parking_lot_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_receipt_content_parking_lot1_idx` (`parking_lot_id` ASC),
  CONSTRAINT `fk_receipt_content_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
