package com.shpp;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataInsertingTest {

    @Test
    void testInsertStores() {
        DataInserting dataInserting = new DataInserting();
        dataInserting.insertStores();

        assertTrue(tableNotEmpty("stores"));
    }

    @Test
    void testInsertProductCategories() {
        DataInserting dataInserting = new DataInserting();
        dataInserting.insertProductCategories();

        assertTrue(tableNotEmpty("categories"));
    }

    private boolean tableNotEmpty(String tableName) {
        try (Connection connection = ConnectorDB.getConnection();
             ResultSet resultSet = connection.createStatement().executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error checking table existence: " + e.getMessage());
        }
    }

}
