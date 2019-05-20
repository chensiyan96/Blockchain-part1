CREATE TABLE `blockchain`.`user` (
  `id` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(64) NOT NULL,
  `phone` VARCHAR(45) NULL,
  `avatar` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;
