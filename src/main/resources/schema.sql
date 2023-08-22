drop table IF EXISTS users CASCADE;
create TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  CONSTRAINT id PRIMARY KEY (id),
  CONSTRAINT email UNIQUE (email)
);

drop table IF EXISTS items CASCADE;
create TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(255) NOT NULL,
  description VARCHAR NOT NULL,
  available BOOLEAN,
  owner_id BIGINT,
  request_id BIGINT,
   CONSTRAINT pk_item PRIMARY KEY (id)
);
drop table IF EXISTS booking CASCADE;
create TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_booking TIMESTAMP,
    end_booking TIMESTAMP,
    booker BIGINT,
    status VARCHAR,
    item_id BIGINT,
    FOREIGN KEY (booker) REFERENCES users (id),
    FOREIGN KEY (item_id) REFERENCES items (id),
     CONSTRAINT pk_booking PRIMARY KEY (id)
);
drop table IF EXISTS comments CASCADE;
create TABLE IF NOT EXISTS comments (
id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
item_id BIGINT,
comment VARCHAR NOT NULL,
item_name VARCHAR,
author_name VARCHAR,
created TIMESTAMP,
FOREIGN KEY (item_id) REFERENCES items (id),
 CONSTRAINT pk_comment PRIMARY KEY (id)
)
