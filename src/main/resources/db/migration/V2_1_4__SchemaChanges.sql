ALTER TABLE `get_my_parking_v2`.`parking_pass`
ADD COLUMN `valid_from` TIMESTAMP NOT NULL;

ALTER TABLE `get_my_parking_v2`.`parking_pass_master`
ADD COLUMN `auto_renewal` INT DEFAULT 0;

ALTER TABLE `get_my_parking_v2`.`user_b2b`
ADD COLUMN `app_version` VARCHAR(50) DEFAULT NULL;

ALTER TABLE `get_my_parking_v2`.`company`
ADD COLUMN `master_password` VARCHAR(500) DEFAULT NULL;


