
ALTER TABLE `get_my_parking_v2`.`parking_pass`
ADD COLUMN `valid_from` TIMESTAMP NULL;

ALTER TABLE `parking_lot` CHANGE `id` `id` INT(11) NOT NULL AUTO_INCREMENT;
ALTER TABLE `receipt_content` CHANGE `id` `id` INT(11) NOT NULL AUTO_INCREMENT;