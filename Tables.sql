CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  email VARCHAR(254) NOT NULL,
  password CHAR(60) NOT NULL,
  
  CONSTRAINT pk_users_id PRIMARY KEY (id)
);

CREATE UNIQUE INDEX unique_users_email ON users (email);