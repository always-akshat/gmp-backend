ALTER TABLE `parking_lot` ADD COLUMN  `barcode_support` INT NOT NULL DEFAULT 0;
ALTER TABLE `parking_lot` CHANGE  `taxi_time` `taxi_time` INT NULL;
ALTER TABLE `receipt_content` ADD COLUMN `vehicle_type` VARCHAR(255) NOT NULL;