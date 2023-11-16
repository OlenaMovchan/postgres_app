package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public void createTable(Connection connection) {
            try (Statement statement = connection.createStatement()) {

                statement.executeUpdate("DROP TABLE IF EXISTS deliveries");
                statement.executeUpdate("DROP TABLE IF EXISTS products");
                statement.executeUpdate("DROP TABLE IF EXISTS stores");
                statement.executeUpdate("DROP TABLE IF EXISTS categories");

                statement.executeUpdate("CREATE TABLE categories ("
                        + "category_id SERIAL PRIMARY KEY,"
                        + "category_name VARCHAR(255) NOT NULL)");

                statement.executeUpdate("CREATE TABLE stores ("
                        + "store_id SERIAL PRIMARY KEY,"
                        + "location VARCHAR(255))");

                statement.executeUpdate("CREATE TABLE products ("
                        + "product_id SERIAL PRIMARY KEY,"
                        + "product_name VARCHAR(255) NOT NULL,"
                        + "category_id BIGINT,"
                        + "FOREIGN KEY (category_id) REFERENCES categories (category_id))");

                statement.executeUpdate("CREATE TABLE deliveries ("
                        + "product_id BIGINT,"
                        + "store_id BIGINT,"
                        + "product_count INTEGER NOT NULL,"
                        + "FOREIGN KEY (product_id) REFERENCES products (product_id),"
                        + "FOREIGN KEY (store_id) REFERENCES stores (store_id))");

                LOGGER.info("Tables created successfully");

            } catch (SQLException e) {
                LOGGER.error("Error creating tables" , e.getMessage());
            }
        }

}

