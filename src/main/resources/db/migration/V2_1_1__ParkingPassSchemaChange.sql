
ALTER TABLE `get_my_parking_v2`.`receipt_content`
ADD COLUMN `parking_pass_master_id` INT NULL;

ALTER TABLE `get_my_parking_v2`.`receipt_content`
ADD FOREIGN KEY (`parking_pass_master_id`)
REFERENCES `get_my_parking_v2`.`parking_pass_master` (`id`);

ALTER TABLE `get_my_parking_v2`.`receipt_content`
CHANGE COLUMN `parking_sub_lot_id` `parking_sub_lot_id` INT NULL ;

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
ADD COLUMN `is_rfid` VARCHAR(50) NULL,
ADD COLUMN `is_paid` VARCHAR(50) NULL;


INSERT INTO `sub_lot_type`(`type_name`) Values ('CAR');
INSERT INTO `sub_lot_type`(`type_name`) Values ('BIKE');
