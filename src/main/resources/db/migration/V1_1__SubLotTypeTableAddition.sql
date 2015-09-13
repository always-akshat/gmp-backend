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

SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
