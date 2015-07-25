ALTER TABLE `parking_lot` ADD COLUMN `taxi_time` TIME NULL;
ALTER TABLE `parking_lot` ADD COLUMN `taxi_support` INT NOT NULL DEFAULT 0;