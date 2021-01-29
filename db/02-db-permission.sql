GRANT SELECT, INSERT, UPDATE, DELETE, TRIGGER
  ON hellodb.auth_user TO 'auth_dbuser'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE, TRIGGER
  ON hellodb.auth_user_id TO 'auth_dbuser'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE, TRIGGER
  ON hellodb.auth_user_role TO 'auth_dbuser'@'%';

FLUSH PRIVILEGES;
