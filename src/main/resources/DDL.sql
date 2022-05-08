DROP TABLE IF EXISTS items;

CREATE TABLE IF NOT EXISTS items (
  id INTEGER AUTO_INCREMENT,
  name TEXT NOT NULL,
  price INTEGER DEFAULT 0,
  stock INTEGER DEFAULT 0,
  PRIMARY KEY (id),
  CHECK (length(name) > 0 AND
    price >= 0 AND
    stock >= 0));