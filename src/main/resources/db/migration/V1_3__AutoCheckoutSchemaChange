ALTER TABLE `parking_lot` ADD COLUMN `auto_checkout_time` TIME NULL;
ALTER TABLE `parking_event` CHANGE id id BIGINT;
ALTER TABLE `parking_event` CHANGE serial_number serial_number VARCHAR(50);
ALTER TABLE `parking_event` CHANGE event_type event_type VARCHAR(50);

CREATE INDEX `registration_number_index` ON `parking_event` (`registration_number`);
CREATE INDEX serial_number_index ON parking_event (`serial_number`);
CREATE INDEX event_time_index ON parking_event (`event_time`);
CREATE INDEX updated_time_index ON parking_event (`updated_time`);