DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS deleted_items;

CREATE TABLE IF NOT EXISTS items (
  id INTEGER PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  price NUMERIC(15,2) DEFAULT 0,
  stock INTEGER DEFAULT 0,
  CHECK (length(name) > 0 AND
    price >= 0 AND
    stock >= 0));

CREATE TABLE IF NOT EXISTS deleted_items (
  id INTEGER UNIQUE,
  name VARCHAR(50) NOT NULL,
  price NUMERIC(15,2),
  stock INTEGER,
  comment TEXT,
  CHECK (length(name) > 0 AND
    price >= 0 AND
    stock >= 0));
