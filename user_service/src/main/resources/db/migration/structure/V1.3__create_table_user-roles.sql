CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY (`role_id`),
  CONSTRAINT  FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)