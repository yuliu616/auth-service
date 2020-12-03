DROP TABLE IF EXISTS `auth_user`;

CREATE TABLE `auth_user` (
  `id` varchar(32) NOT NULL,
  `version` bigint(20) NOT NULL DEFAULT 1,
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `last_updated` datetime DEFAULT CURRENT_TIMESTAMP,
  `username` varchar(120) NOT NULL,
  `passwordHash` varchar(128) DEFAULT NULL,
  `is_active` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `auth_user`
ADD INDEX `ix_auth_user_username` (`username` ASC);
