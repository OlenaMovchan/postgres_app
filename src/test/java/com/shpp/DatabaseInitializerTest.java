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

    @BeforeEach
    void setup() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        ConnectorDB.setDataSource(dataSource, "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
    }

    @Test
    void testCreateTables() {
        //DatabaseInitializer initializer = new DatabaseInitializer();

        //initializer.createTables();

        assertAll(
                () -> assertTrue(tableExists("categories")),
                () -> assertTrue(tableExists("stores")),
                () -> assertTrue(tableExists("products")),
                () -> assertTrue(tableExists("store_products"))
        );
    }

    @Test
    void testCreateFalseTable() {
        //DatabaseInitializer initializer = new DatabaseInitializer();

       // initializer.createTables();

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
