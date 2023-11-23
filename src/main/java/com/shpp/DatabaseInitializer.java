package com.shpp;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;

public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String SCRIPT_FILE = "createTables.sql";
    private static final String SCRIPT_DELIMITER = ";";

    public void createTables() {
        try (Connection connection = ConnectorDB.getConnection();
             Statement statement = connection.createStatement()) {

            executeScript(statement, readScriptFile());

            LOGGER.info("Tables created successfully");

        } catch (SQLException e) {
            LOGGER.error("Error creating tables", e);
        }
    }
//public void createTables() {
//    Connection connection = null;
//    try {
//        connection = ConnectorDB.getConnection();
//        connection.setAutoCommit(false);
//
//        Statement statement = connection.createStatement();
//
//        executeScript(statement, readScriptFile());
//
//        connection.commit();
//
//        LOGGER.info("Tables created successfully");
//
//    } catch (SQLException e) {
//        LOGGER.error("Error creating tables", e);
//
//
//        try {
//            if (connection != null) {
//                connection.rollback();
//            }
//        } catch (SQLException rollbackException) {
//            LOGGER.error("Error rolling back transaction", rollbackException);
//        }
//
//    } finally {
//
//        if (connection != null) {
//            try {
//                connection.close();
//            } catch (SQLException closeException) {
//                LOGGER.error("Error closing connection", closeException);
//            }
//        }
//    }
//}
    public void executeScript(Statement statement, String script) throws SQLException {
        String[] queries = script.split(SCRIPT_DELIMITER);
        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                statement.executeUpdate(query.trim());
            }
        }
    }

    public String readScriptFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + SCRIPT_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append(System.lineSeparator());
            }
            return script.toString();

        } catch (IOException e) {
            LOGGER.error("Error reading script file", e);
            return "";
        }
    }
}
//public class DatabaseInitializer {
//    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
//    public void createTables() {
//            try (Connection connection = ConnectorDB.getConnection();
//                 Statement statement = connection.createStatement()) {
//
//                statement.executeUpdate("DROP TABLE IF EXISTS deliveries CASCADE");
//                statement.executeUpdate("DROP TABLE IF EXISTS products CASCADE");
//                statement.executeUpdate("DROP TABLE IF EXISTS stores CASCADE");
//                statement.executeUpdate("DROP TABLE IF EXISTS categories CASCADE");
//
//                //statement.executeUpdate("DROP INDEX idx_categories_category_id");
//                //statement.executeUpdate("DROP INDEX idx_products_category_id");
//                //statement.executeUpdate("DROP INDEX idx_deliveries_product_id");
//                //statement.executeUpdate("DROP INDEX idx_deliveries_store_id");
//                //statement.executeUpdate("DROP INDEX idx_stores_store_id");
//
//                statement.executeUpdate("CREATE TABLE categories ("
//                        + "category_id SERIAL PRIMARY KEY,"
//                        + "category_name VARCHAR(255) NOT NULL)");
//
//                statement.executeUpdate("CREATE TABLE stores ("
//                        + "store_id SERIAL PRIMARY KEY,"
//                        + "location VARCHAR(255))");
//
//                statement.executeUpdate("CREATE TABLE products ("
//                        + "product_id SERIAL PRIMARY KEY,"
//                        + "product_name VARCHAR(255) NOT NULL,"
//                        + "category_id BIGINT,"
//                        + "FOREIGN KEY (category_id) REFERENCES categories (category_id))");
//
//                statement.executeUpdate("CREATE TABLE deliveries ("
//                        + "product_id BIGINT,"
//                        + "store_id BIGINT,"
//                        + "product_count INTEGER NOT NULL,"
//                        + "FOREIGN KEY (product_id) REFERENCES products (product_id),"
//                        + "FOREIGN KEY (store_id) REFERENCES stores (store_id))");
//
//                LOGGER.info("Tables created successfully");
//
//            } catch (SQLException e) {
//                LOGGER.error("Error creating tables" , e.getMessage());
//            }
//        }

//    static boolean tableExists(Connection connection, String tableName) throws SQLException {
//        DatabaseMetaData meta = connection.getMetaData();
//        ResultSet resultSet = meta.getTables(null, null, tableName, new String[] {"stores"});
//
//        return resultSet.next();
//    }
//    public void executeDdlScript(String fileName) {
//        FileReader fileReader = getFileReader(fileName);
//        try (BufferedReader reader = new BufferedReader(fileReader);
//             Connection connection = ConnectorDB.getConnection()) {
//            LOGGER.info("DDL script from file: {}", fileName);
//            ScriptRunner runner = new ScriptRunner(connection);
//            runner.setStopOnError(true);
//            runner.runScript(reader);
//            LOGGER.info("Tables created successfully");
//        } catch (IOException | SQLException e) {
//            LOGGER.error("Error creating tables", e.getMessage());
//        }
//    }

//    private FileReader getFileReader(String fileName) {
//        try {
//            return new FileReader(fileName);
//        } catch (FileNotFoundException e) {
//            try {
//                return new FileReader(fileName);
//            } catch (FileNotFoundException ex) {
//                throw new RuntimeException(ex);
//            }
//        }
//    }

//}

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

