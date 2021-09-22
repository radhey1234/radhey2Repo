ALTER TABLE `iexpress`.`question` 
DROP FOREIGN KEY `question_post_id`;
ALTER TABLE `iexpress`.`question` 
CHANGE COLUMN `post_id` `post_id` INT(10) UNSIGNED NULL ;
ALTER TABLE `iexpress`.`question` 
ADD CONSTRAINT `question_post_id`
  FOREIGN KEY (`post_id`)
  REFERENCES `iexpress`.`post` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
