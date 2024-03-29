package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IndexCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexCreator.class);

    public void createIndex() {
        try (Connection connection = ConnectorDB.getConnection();
             Statement statement = connection.createStatement()) {
createIndex(statement, "idx_pr_cat", "products", "category_id, product_id");
            createIndex(statement, "idx_categories_category_id", "categories", "category_id");
            createIndex(statement, "idx_categories_category_name", "categories", "category_name");

            createIndex(statement, "idx_products_category_id", "products", "category_id");

            createIndex(statement, "idx_store_quantity", "store_products", "store_id, quantity");
            createIndex(statement, "idx_product_quantity", "store_products", "product_id, quantity");


            createIndex(statement, "idx_store_products_store_id", "store_products", "store_id");
            createIndex(statement, "idx_store_products_product_id", "store_products", "product_id");

            createIndex(statement, "idx_stores_store_id", "stores", "store_id");

            LOGGER.info("Indexes created successfully");

        } catch (SQLException e) {
            LOGGER.error("Error creating", e.getMessage());
        }
    }

    private void createIndex(Statement statement, String indexName, String tableName, String columnName) throws SQLException {
        String sql = "CREATE INDEX " + indexName + " ON " + tableName + " (" + columnName + ")";
        statement.executeUpdate(sql);
        LOGGER.info("Index " + indexName + " created on table " + tableName + " column " + columnName);
    }
}

