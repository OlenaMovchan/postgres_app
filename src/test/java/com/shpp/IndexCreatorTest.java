package com.shpp;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IndexCreatorTest {

    @Test
    void testCreateIndexes() {
        IndexCreator indexCreator = new IndexCreator();
        indexCreator.createIndex();

        String expectedIndexes = "idx_stores_store_id";

        try (Connection connection = ConnectorDB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            assertTrue(indexExists(metaData, expectedIndexes), "Index " + expectedIndexes + " not found");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean indexExists(DatabaseMetaData metaData, String indexName) throws SQLException {
        try (ResultSet resultSet = metaData.getIndexInfo(null, null, "stores", false, false)) {
            while (resultSet.next()) {
                String currentIndexName = resultSet.getString("INDEX_NAME");
                if (currentIndexName != null && currentIndexName.equals(indexName)) {
                    return true;
                }
            }
        }
        return false;
    }
}

