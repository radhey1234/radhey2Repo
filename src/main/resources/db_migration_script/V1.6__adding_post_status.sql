ALTER TABLE `iexpress`.`post` 
ADD COLUMN `status` ENUM('DRAFT', 'PUBLISHED') NOT NULL DEFAULT 'DRAFT' AFTER `modified_at`;
