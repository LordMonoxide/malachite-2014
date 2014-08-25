CREATE TABLE users (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(254) NOT NULL,
  password CHAR(60) NOT NULL,
  
  CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX unique_users_email ON users (email);

CREATE TABLE characters (
  id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  name VARCHAR(20) NOT NULL,
  
  CONSTRAINT pk_characters_id PRIMARY KEY (id),
  CONSTRAINT fk_user_id       FOREIGN KEY (user_id) REFERENCES users(id)
);