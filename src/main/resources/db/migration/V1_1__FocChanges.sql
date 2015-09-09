
CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`foc_reasons` (
  `title` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`title`))
  ENGINE = InnoDB;


CREATE TABLE IF NOT EXISTS `get_my_parking_v2`.`parking_lot_foc_reasons` (
  `foc_reasons_title` VARCHAR(255) NOT NULL,
  `parking_lot_id` INT NOT NULL,
  `id` INT NOT NULL AUTO_INCREMENT,
  INDEX `fk_foc_reasons_has_parking_lot_parking_lot1_idx` (`parking_lot_id` ASC),
  INDEX `fk_foc_reasons_has_parking_lot_foc_reasons1_idx` (`foc_reasons_title` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_foc_reasons_has_parking_lot_foc_reasons1`
  FOREIGN KEY (`foc_reasons_title`)
  REFERENCES `get_my_parking_v2`.`foc_reasons` (`title`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_foc_reasons_has_parking_lot_parking_lot1`
  FOREIGN KEY (`parking_lot_id`)
  REFERENCES `get_my_parking_v2`.`parking_lot` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
  ENGINE = InnoDB;

ALTER TABLE `get_my_parking_v2`.`parking_event`
  ADD COLUMN `special` VARCHAR(255) NULL,
  ADD COLUMN `foc_reason` VARCHAR(255) NULL,
  ADD INDEX `fk_parking_event_foc_reasons1_idx` (`foc_reason` ASC),
  ADD CONSTRAINT `fk_parking_event_foc_reasons1`
    FOREIGN KEY (`foc_reason`) REFERENCES `get_my_parking_v2`.`foc_reasons` (`title`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
