CREATE TABLE `roles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` enum('ADMIN','CUSTOMER','MANAGER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
)
