DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT id PRIMARY KEY (id),
  CONSTRAINT email UNIQUE (email)
);

DROP TABLE IF EXISTS items CASCADE;
CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR NOT NULL,
  available BOOLEAN,
  owner_id INTEGER,
  request_id INTEGER
);
DROP TABLE IF EXISTS booking CASCADE;
CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_booking TIMESTAMP,
    end_booking TIMESTAMP,
    booker INTEGER,
    status VARCHAR
);