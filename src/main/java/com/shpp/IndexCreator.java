package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class IndexCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(IndexCreator.class);

    public void createIndex(Connection connection) {
        try (Statement statement = connection.createStatement()) {
//primary key auto index???
            createIndex(statement, "idx_categories_category_id", "categories", "category_id");
            createIndex(statement, "idx_products_category_id", "products", "category_id");
            createIndex(statement, "idx_deliveries_product_id", "deliveries", "product_id");
            createIndex(statement, "idx_deliveries_store_id", "deliveries", "store_id");
            createIndex(statement, "idx_stores_store_id", "stores", "store_id");

            LOGGER.info("Indexes created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createIndex(Statement statement, String indexName, String tableName, String columnName) throws SQLException {
        String sql = "CREATE INDEX " + indexName + " ON " + tableName + " (" + columnName + ")";
        statement.executeUpdate(sql);
        LOGGER.info("Index " + indexName + " created on table " + tableName + " column " + columnName);
    }
}

