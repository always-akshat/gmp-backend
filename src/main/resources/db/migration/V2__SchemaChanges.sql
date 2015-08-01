ALTER TABLE `receipt_content` ADD COLUMN `receipt_section` VARCHAR(55) NOT NULL;
ALTER TABLE `receipt_content` ADD COLUMN `sequence` INT NOT NULL;
ALTER TABLE `receipt_content` ADD COLUMN `flag` INT NOT NULL;
ALTER TABLE `receipt_content` ADD COLUMN `event` VARCHAR(255) NOT NULL;

ALTER TABLE `parking_lot` ADD COLUMN `parking_lot_type` VARCHAR(255) NOT NULL;
ALTER TABLE `parking` ADD COLUMN `report_email_address` VARCHAR(500) NOT NULL;
ALTER TABLE `parking_lot` ADD COLUMN `checkin_barcode` INT NOT NULL;
ALTER TABLE `parking_lot` ADD COLUMN `checkout_barcode` INT NOT NULL;

