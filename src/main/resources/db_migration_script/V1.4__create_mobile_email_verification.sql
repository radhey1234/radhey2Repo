CREATE TABLE `iexpress`.`mobile_email_verification` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(95) NULL,
  `mobile` VARCHAR(45) NULL,
  `otp` VARCHAR(45) NULL,
  `status` ENUM('Pending', 'Blocked', 'Active') NULL,
  `created_at` DATETIME NULL,
  `modified_at` DATETIME NULL,
  `randomeToken` VARCHAR(255) NULL,
  PRIMARY KEY (`id`));
