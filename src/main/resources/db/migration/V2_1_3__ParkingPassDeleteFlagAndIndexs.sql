
ALTER TABLE `get_my_parking_v2`.`parking_pass`
ADD COLUMN `is_deleted` INT NULL;

ALTER TABLE `parking_event` ADD INDEX `serialNumberIndex` (`serial_number`);
ALTER TABLE `parking_event` ADD INDEX `eventTimeIndex` (`event_time`);
ALTER TABLE `parking_event` ADD INDEX `parkingIdEventTimeIndex` (`event_time`,`parking_id`);
