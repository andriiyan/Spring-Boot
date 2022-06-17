CREATE TABLE IF NOT EXISTS event(
    id INT NOT NULL auto_increment,
    title character varying(255) NOT NULL,
    "date" date NOT NULL,
    "time" time without time zone NOT NULL,
    ticket_price real NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT title_date_unique UNIQUE (title, "date")
 );

 CREATE TABLE  IF NOT EXISTS users(
     id INT NOT NULL auto_increment,
     name character varying(255) NOT NULL,
     email character varying(255) NOT NULL,
     password character varying(255) NOT NULL,
     CONSTRAINT unique_name UNIQUE (name),
     CONSTRAINT unique_email UNIQUE (email),
     PRIMARY KEY (id)
 );

 CREATE TABLE IF NOT EXISTS ticket(
     id INT NOT NULL auto_increment,
     user_id INT NOT NULL REFERENCES users(id),
     event_id INT NOT NULL REFERENCES event(id),
     category character varying(50) NOT NULL,
     place INT NOT NULL,
     PRIMARY KEY (id),
     CONSTRAINT event_place_unique UNIQUE (event_id, place)
 );

 CREATE TABLE IF NOT EXISTS user_account(
     id INT NOT NULL auto_increment,
     user_id INT NOT NULL REFERENCES users(id),
     amount real NOT NULL DEFAULT 0,
     PRIMARY KEY (id)
 );
