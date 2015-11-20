
ALTER TABLE `get_my_parking_v2`.`parking_pass`
ADD COLUMN `valid_from` TIMESTAMP NULL;

ALTER TABLE parking_sub_lot DROP FOREIGN KEY `fk_parking_sub_lot_parking_lot1`;
ALTER TABLE parking_event DROP FOREIGN KEY `fk_parking_event_parking_lot1`;
ALTER TABLE parking_sub_lot_user_access DROP FOREIGN KEY `fk_parking_sub_lot_user_access_parking_lot1`;
ALTER TABLE `parking_lot` CHANGE `id` `id` INT(11) NOT NULL AUTO_INCREMENT;

ALTER TABLE parking_sub_lot ADD CONSTRAINT `fk_parking_sub_lot_parking_lot1`
FOREIGN KEY (`parking_lot_id`) REFERENCES `parking_lot` (`id`);
ALTER TABLE parking_event ADD CONSTRAINT `fk_parking_event_parking_lot1`
FOREIGN KEY (`parking_lot_id`) REFERENCES `parking_lot` (`id`);
ALTER TABLE parking_sub_lot_user_access ADD CONSTRAINT `fk_parking_sub_lot_user_access_parking_lot1`
FOREIGN KEY (`parking_lot_id`) REFERENCES `parking_lot` (`id`);


ALTER TABLE `receipt_content` CHANGE `id` `id` INT(11) NOT NULL AUTO_INCREMENT;