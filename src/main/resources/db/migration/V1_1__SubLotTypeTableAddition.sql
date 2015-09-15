SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`sub_lot_type` (
  `type_name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`type_name`))
ENGINE = InnoDB;

ALTER TABLE `get_my_parking_v2`.`parking_sub_lot`
  ADD FOREIGN KEY (`type`)
  REFERENCES `get_my_parking_v2`.`sub_lot_type` (`type_name`);

ALTER TABLE `get_my_parking_v2`.`parking_event`
ADD FOREIGN KEY (`sub_lot_type`)
REFERENCES `get_my_parking_v2`.`sub_lot_type` (`type_name`);

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
ADD FOREIGN KEY (`vehicle_type`)
REFERENCES `get_my_parking_v2`.`sub_lot_type` (`type_name`);

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
DROP FOREIGN KEY `fk_parking_pass_parking_lot1`;

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
DROP COLUMN parking_sub_lot_id;

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
ADD COLUMN `parking_id` INT NOT NULL;

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
ADD FOREIGN KEY (`parking_id`)
REFERENCES `get_my_parking_v2`.`parking` (`id`)
ON DELETE NO ACTION
ON UPDATE NO ACTION;

ALTER TABLE `get_my_parking_v2`.`parking_pass`
ADD COLUMN `operator_name` VARCHAR(255) NULL,
ADD COLUMN `mobile_number` VARCHAR(255) NULL,
ADD COLUMN `cost` INT NOT NULL,
ADD COLUMN `card_id` VARCHAR(255) NULL,
ADD COLUMN `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN `is_paid` INT NOT NULL;

ALTER TABLE `get_my_parking_v2`.`parking_event`
ADD COLUMN `special` VARCHAR(255) NULL,
ADD COLUMN `foc_reason` VARCHAR(255) NULL;

ALTER TABLE `get_my_parking_v2`.`parking_event`
ADD FOREIGN KEY (`foc_reason`)
REFERENCES `get_my_parking_v2`.`foc_reasons` (`reason_title`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`foc_reasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`foc_reasons` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`foc_reasons` (
  `reason_title` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`reason_title`))
  ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `get_my_parking_v2`.`foc_reasons_has_parking_lot`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `get_my_parking_v2`.`foc_reasons_has_parking_lot` ;

CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`foc_reasons_has_parking_lot` (
  `foc_reasons_reason_title` VARCHAR(255) NOT NULL,
  `parking_sub_lot_id` INT NOT NULL,
  PRIMARY KEY (`foc_reasons_reason_title`, `parking_sub_lot_id`),
  INDEX `fk_foc_reasons_has_parking_lot_parking_lot1_idx` (`parking_sub_lot_id` ASC),
  INDEX `fk_foc_reasons_has_parking_lot_foc_reasons1_idx` (`foc_reasons_reason_title` ASC),
  CONSTRAINT `fk_foc_reasons_has_parking_lot_foc_reasons1`
  FOREIGN KEY (`foc_reasons_reason_title`)
  REFERENCES `get_my_parking_v2`.`foc_reasons` (`reason_title`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_foc_reasons_has_parking_lot_parking_lot1`
  FOREIGN KEY (`parking_sub_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_sub_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
