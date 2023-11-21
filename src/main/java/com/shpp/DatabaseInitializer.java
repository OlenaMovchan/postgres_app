package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public void createTable() {
            try (Connection connection = ConnectorDB.getConnection();
                 Statement statement = connection.createStatement()) {

                statement.executeUpdate("DROP TABLE IF EXISTS deliveries CASCADE");
                statement.executeUpdate("DROP TABLE IF EXISTS products CASCADE");
                statement.executeUpdate("DROP TABLE IF EXISTS stores CASCADE");
                statement.executeUpdate("DROP TABLE IF EXISTS categories CASCADE");

                //statement.executeUpdate("DROP INDEX idx_categories_category_id");
                //statement.executeUpdate("DROP INDEX idx_products_category_id");
                //statement.executeUpdate("DROP INDEX idx_deliveries_product_id");
                //statement.executeUpdate("DROP INDEX idx_deliveries_store_id");
                //statement.executeUpdate("DROP INDEX idx_stores_store_id");

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

//package net.javaguides.postgresql.tutorial;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// * Create Table JDBC Example
// * @author Ramesh Fadatare
// *
// */
//public class CreateTableExample {
//
//    private final String url = "jdbc:postgresql://localhost/mydb";
//    private final String user = "postgres";
//    private final String password = "root";
//
//    private static final String createTableSQL = "CREATE TABLE users " +
//        "(ID INT PRIMARY KEY ," +
//        " NAME TEXT, " +
//        " EMAIL VARCHAR(50), " +
//        " COUNTRY VARCHAR(50), " +
//        " PASSWORD VARCHAR(50))";
//
//    public static void main(String[] argv) throws SQLException {
//        CreateTableExample createTableExample = new CreateTableExample();
//        createTableExample.createTable();
//    }
//
//    public void createTable() throws SQLException {
//
//        System.out.println(createTableSQL);
//        // Step 1: Establishing a Connection
//        try (Connection connection = DriverManager.getConnection(url, user, password);
//
//            // Step 2:Create a statement using connection object
//            Statement statement = connection.createStatement();) {
//
//            // Step 3: Execute the query or update query
//            statement.execute(createTableSQL);
//        } catch (SQLException e) {
//
//            // print SQL exception information
//            printSQLException(e);
//        }
//    }
//
//    public static void printSQLException(SQLException ex) {
//        for (Throwable e: ex) {
//            if (e instanceof SQLException) {
//                e.printStackTrace(System.err);
//                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//                System.err.println("Message: " + e.getMessage());
//                Throwable t = ex.getCause();
//                while (t != null) {
//                    System.out.println("Cause: " + t);
//                    t = t.getCause();
//                }
//            }
//        }
//    }
//}

