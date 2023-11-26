package com.shpp;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TableColumnTest {

    @Test
    void testTableCategoriesColumns() {
        String tableName = "categories";
        Set<String> expectedColumns = Set.of(
                "category_id",
                "category_name"

        );

        try (Connection connection = ConnectorDB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Set<String> actualColumns = getTableColumns(metaData, tableName);

            assertTrue(actualColumns.containsAll(expectedColumns),
                    "Not all expected columns are present in the table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTableDeliveriesColumns() {
        String tableName = "store_products";
        Set<String> expectedColumns = Set.of(
                "store_id",
                "product_id",
                "quantity"

        );

        try (Connection connection = ConnectorDB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Set<String> actualColumns = getTableColumns(metaData, tableName);

            assertTrue(actualColumns.containsAll(expectedColumns),
                    "Not all expected columns are present in the table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTableProductsColumns() {
        String tableName = "products";
        Set<String> expectedColumns = Set.of(
                "product_id",
                "product_name",
                "category_id"

        );

        try (Connection connection = ConnectorDB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Set<String> actualColumns = getTableColumns(metaData, tableName);

            assertTrue(actualColumns.containsAll(expectedColumns),
                    "Not all expected columns are present in the table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testTableStoresColumns() {
        String tableName = "stores";
        Set<String> expectedColumns = Set.of(
                "store_id",
                "location"

        );

        try (Connection connection = ConnectorDB.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            Set<String> actualColumns = getTableColumns(metaData, tableName);

            assertTrue(actualColumns.containsAll(expectedColumns),
                    "Not all expected columns are present in the table");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Set<String> getTableColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        Set<String> columns = new HashSet<>();

        try (ResultSet resultSet = metaData.getColumns(null, null, tableName, null)) {
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columns.add(columnName);
            }
        }

        return columns;
    }
}
