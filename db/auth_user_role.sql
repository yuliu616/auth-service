DROP TABLE IF EXISTS `auth_user_role`;

CREATE TABLE `auth_user_role` (
  `user_id` varchar(32) NOT NULL,
  `role` varchar(256) NOT NULL,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `auth_user_role`
ADD INDEX `ix_auth_user_role_user_id` (`user_id` ASC);

ALTER TABLE `auth_user_role`
ADD INDEX `ix_auth_user_role_role` (`role` ASC);
