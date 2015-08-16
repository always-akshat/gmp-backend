SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema get_my_parking_v2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `get_my_parking_v2` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `get_my_parking_v2` ;

-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`company`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`company` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`company` (
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
-- Table `get_my_parking_v2`.`parking`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking` (
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
  REFERENCES `get_my_parking_v2`.`company` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_lot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_lot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_lot` (
  `id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `open_time` TIME NOT NULL,
  `close_time` TIME NOT NULL,
  `longitude` DECIMAL(10,4) NOT NULL,
  `latitude` DECIMAL(10,4) NOT NULL,
  `parking_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_lot_parking1_idx` (`parking_id` ASC),
  CONSTRAINT `fk_parking_lot_parking1`
  FOREIGN KEY (`parking_id`)
  REFERENCES `get_my_parking_v2`.`parking` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_sub_lot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_sub_lot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_sub_lot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(255) NOT NULL,
  `capacity` INT NOT NULL,
  `collection_model` VARCHAR(500) NOT NULL COMMENT 'PREPAID / POSTPAID / HYBRID',
  `taxi_time` TIME NOT NULL,
  `auto_checkout_time` TIME NOT NULL,
  `parking_lot_id` INT NOT NULL,
  `plate_number_type` VARCHAR(45) NOT NULL,
  `mobile_required` TINYINT NOT NULL,
  `valet_name` TINYINT NOT NULL,
  `last_checkin_update_time` TIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_sub_lot_parking_lot1_idx` (`parking_lot_id` ASC),
  CONSTRAINT `fk_parking_sub_lot_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_pass_master`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_pass_master` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_pass_master` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NOT NULL,
  `pass_type` VARCHAR(45) NOT NULL COMMENT 'Weekly / Monthly / Daily / Hourly',
  `numbers` INT NOT NULL,
  `price` INT NOT NULL,
  `parking_sub_lot_id` INT NOT NULL,
  `vehicle_type` VARCHAR(45) NOT NULL,
  `isActive` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_pass_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  CONSTRAINT `fk_parking_pass_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_pass`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_pass` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_pass` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `registration_number` VARCHAR(500) NOT NULL,
  `valid_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `parking_pass_master_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parking_pass_parking_pass_master1_idx` (`parking_pass_master_id` ASC),
  CONSTRAINT `fk_parking_pass_parking_pass_master1`
  FOREIGN KEY (`parking_pass_master_id`)
  REFERENCES `get_my_parking_v2`.`parking_pass_master` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_event`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_event` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_event` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL COMMENT 'Standard / Pass',
  `registration_number` VARCHAR(45) NOT NULL,
  `mobile_number` VARCHAR(45) NULL,
  `valet_name` VARCHAR(255) NULL,
  `event_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `event_type` VARCHAR(500) NOT NULL,
  `cost` DECIMAL(10,2) NOT NULL,
  `parking_sub_lot_id` INT NOT NULL,
  `sub_lot_type` VARCHAR(45) NOT NULL,
  `parking_pass_id` INT NULL,
  `serial_number` VARCHAR(500) NOT NULL,
  `shift_number` VARCHAR(500) NOT NULL,
  `updated_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `operator_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parkings_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  INDEX `fk_parkings_parking_pass1_idx` (`parking_pass_id` ASC),
  CONSTRAINT `fk_parkings_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parkings_parking_pass1`
  FOREIGN KEY (`parking_pass_id`)
  REFERENCES `get_my_parking_v2`.`parking_pass` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`pricing_slot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`pricing_slot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`pricing_slot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `day` INT NOT NULL,
  `start_minutes_of_day` INT NOT NULL,
  `end_minutes_of_day` INT NOT NULL,
  `parking_sub_lot_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_pricing_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  CONSTRAINT `fk_pricing_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`price_grid`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`price_grid` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`price_grid` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `price_structure` VARCHAR(45) NOT NULL COMMENT 'INCREMENTAL / FLAT',
  `cost` INT NOT NULL,
  `duration` INT NOT NULL,
  `pricing_id` INT NOT NULL,
  `sequence_number` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_price_grid_parking_lot_price1_idx` (`pricing_id` ASC),
  CONSTRAINT `fk_price_grid_parking_lot_price1`
  FOREIGN KEY (`pricing_id`)
  REFERENCES `get_my_parking_v2`.`pricing_slot` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`user_b2b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`user_b2b` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`user_b2b` (
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(1000) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `contact_number` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`username`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`parking_lot_has_user_b2b`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`parking_lot_has_user_b2b` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_lot_has_user_b2b` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `parking_sub_lot_id` INT NOT NULL,
  `user_b2b_username` VARCHAR(255) NOT NULL,
  INDEX `fk_parking_lot_has_user_b2b_user_b2b1_idx` (`user_b2b_username` ASC),
  INDEX `fk_parking_lot_has_user_b2b_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_parking_lot_has_user_b2b_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_parking_lot_has_user_b2b_user_b2b1`
  FOREIGN KEY (`user_b2b_username`)
  REFERENCES `get_my_parking_v2`.`user_b2b` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`session`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`session` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`session` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `auth_token` VARCHAR(1000) NOT NULL,
  `valid_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `fk_session_user_b2b1_idx` (`username` ASC),
  CONSTRAINT `fk_session_user_b2b1`
  FOREIGN KEY (`username`)
  REFERENCES `get_my_parking_v2`.`user_b2b` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`style_master`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`style_master` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`style_master` (
  `id` INT NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`receipt_content`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`receipt_content` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`receipt_content` (
  `id` INT NOT NULL,
  `content` VARCHAR(500) NOT NULL,
  `sequence` INT NOT NULL,
  `event_type` VARCHAR(45) NOT NULL,
  `parking_sub_lot_id` INT NOT NULL,
  `style_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_receipt_content_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  INDEX `fk_receipt_content_style_master1_idx` (`style_id` ASC),
  CONSTRAINT `fk_receipt_content_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_receipt_content_style_master1`
  FOREIGN KEY (`style_id`)
  REFERENCES `get_my_parking_v2`.`style_master` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`user_access`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`user_access` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`user_access` (
  `access_title` VARCHAR(128) NOT NULL,
  `user_b2b_username` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`access_title`),
  INDEX `fk_user_access_user_b2b1_idx` (`user_b2b_username` ASC),
  CONSTRAINT `fk_user_access_user_b2b1`
  FOREIGN KEY (`user_b2b_username`)
  REFERENCES `get_my_parking_v2`.`user_b2b` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
