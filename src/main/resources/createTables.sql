DROP TABLE IF EXISTS categories CASCADE ;
DROP TABLE IF EXISTS stores CASCADE ;
DROP TABLE IF EXISTS products CASCADE ;
DROP TABLE IF EXISTS store_products CASCADE ;

CREATE TABLE categories
(
    category_id   SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL

);
CREATE TABLE stores
(
    store_id   SERIAL PRIMARY KEY,
    location   VARCHAR(255)
);
CREATE TABLE products
(
    product_id   SERIAL PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    category_id  BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories (category_id)
);
CREATE TABLE store_products
(
    store_id   BIGINT,
    product_id BIGINT,
    quantity   INT,
    PRIMARY KEY (store_id, product_id),
    FOREIGN KEY (store_id) REFERENCES stores (store_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);