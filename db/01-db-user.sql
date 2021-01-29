DROP USER IF EXISTS 'auth_dbuser'@'%';

CREATE USER 'auth_dbuser'@'%' IDENTIFIED BY 'pass1234';

FLUSH PRIVILEGES;
