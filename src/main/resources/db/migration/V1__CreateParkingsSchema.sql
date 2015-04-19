SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema get_my_parking
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `get_my_parking` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `get_my_parking` ;

-- -----------------------------------------------------
-- Table `get_my_parking`.`parking_lot`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `get_my_parking`.`parking_lot` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(2048) NOT NULL,
  `car_capacity` INT NOT NULL,
  `bike_capacity` INT NOT NULL,
  PRIMARY KEY (`id`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking`.`parkings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `get_my_parking`.`parkings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(45) NOT NULL,
  `registration_number` VARCHAR(45) NOT NULL,
  `checkin_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `checkout_time` TIMESTAMP NULL,
  `parking_lot_id` INT NOT NULL,
  `parking_serial_number` VARCHAR(100) NOT NULL,
  `cost` INT NULL,
  `status` VARCHAR(50) NOT NULL,
  `valid_till_time` TIMESTAMP NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_parkings_parking_lot_idx` (`parking_lot_id` ASC),
  CONSTRAINT `fk_parkings_parking_lot`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
