
ALTER TABLE `get_my_parking_v2`.`session`
ADD COLUMN `last_transaction_time` TIMESTAMP NULL;

ALTER TABLE `get_my_parking_v2`.`session`
ADD COLUMN `last_access_time` TIMESTAMP NULL;

ALTER TABLE `get_my_parking_v2`.`session`
ADD COLUMN `device_id` VARCHAR(255) NULL;

ALTER TABLE `get_my_parking_v2`.`session`
ADD COLUMN `last_accessed_parking_lot_id` INT NULL;

ALTER TABLE `get_my_parking_v2`.`session`
ADD COLUMN `transaction_count` INT DEFAULT 0;

ALTER TABLE `get_my_parking_v2`.`session`
ADD CONSTRAINT `fk_session_parking_lot1`
FOREIGN KEY (`last_accessed_parking_lot_id`) REFERENCES `parking_lot` (`id`);