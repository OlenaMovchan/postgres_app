package com.shpp;

import org.h2.engine.Database;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseInitializerTest {

    @Test
    void testCreateTables() {
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.createTables("DROP TABLE IF EXISTS storestest CASCADE");
        initializer.createTables("DROP TABLE IF EXISTS categoriestest CASCADE");

        initializer.createTables("CREATE TABLE storesTest ("
                + "store_id SERIAL PRIMARY KEY,"
                + "location VARCHAR(255))");
        initializer.createTables("CREATE TABLE categoriesTest ("
                + "category_id SERIAL PRIMARY KEY,"
                + "category_name VARCHAR(255) NOT NULL)");

        assertTrue(tableExists("categoriestest"));
        assertTrue(tableExists("storestest"));

    }

    @Test
    void testCreateFalseTable() {
        DatabaseInitializer initializer = new DatabaseInitializer();
        initializer.createTables("DROP TABLE IF EXISTS storesTest CASCADE");
        initializer.createTables("CREATE TABLE storesTest ("
                + "store_id SERIAL PRIMARY KEY,"
                + "location VARCHAR(255))");
        assertAll(
                () -> assertFalse(tableExists("bad_categories")),
                () -> assertFalse(tableExists("bad_stores")),
                () -> assertFalse(tableExists("bad_products")),
                () -> assertFalse(tableExists("bad_deliveries"))
        );
    }

    private boolean tableExists(String tableName) {
        try (Connection connection = ConnectorDB.getConnection();
             ResultSet tables = connection.getMetaData().getTables(null, null, tableName, null)) {
            return tables.next();
        } catch (SQLException e) {
            fail("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

//    @AfterEach
//    void rollback() {
//        try (Connection connection = ConnectorDB.getConnection()) {
//            connection.rollback();
//        } catch (SQLException e) {
//            fail("Error rolling back transaction: " + e.getMessage());
//        }
//    }

}
