package com.shpp.dop;

import com.github.javafaker.Faker;

import java.sql.*;
import java.util.Locale;
import java.util.Random;

public class DataGenerator {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://database-1.cthx9ogfzrzo.eu-central-1.rds.amazonaws.com:5432/postgres";
        String jdbcUser = "postgres";
        String jdbcPassword = "qazwsx123";

        int numStores = 500;
        int numCategories = 1000;
        int numProducts = 100000;

        try (Connection connection = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
            insertStores(connection, numStores);
            insertCategories(connection, numCategories);
            insertProducts(connection, numProducts, numCategories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void insertStores(Connection connection, int numStores) throws SQLException {
        String insertStoreSQL = "INSERT INTO stores (store_name, location) VALUES (?, ?)";
        Random random = new Random();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStoreSQL)) {
            for (int i = 0; i < numStores; i++) {
                String storeName = "Store " + (i + 1);
                String location = "Location " + (i + 1);
                preparedStatement.setString(1, storeName);
                preparedStatement.setString(2, location);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void insertCategories(Connection connection, int numCategories) throws SQLException {
        String insertCategorySQL = "INSERT INTO categories (category_name) VALUES (?)";
        Faker faker = new Faker(new Locale("uk"));
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            for (int i = 0; i < numCategories; i++) {
                String categoryName = faker.commerce().department();
                preparedStatement.setString(1, categoryName);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }

    private static void insertProducts(Connection connection, int numProducts, int numCategories) throws SQLException {
        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
        Random random = new Random();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            for (int i = 0; i < numProducts; i++) {
                String productName = "Product " + (i + 1);
                int categoryId = random.nextInt(numCategories) + 1; // Categories are 1-indexed
                preparedStatement.setString(1, productName);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        }
    }
//    private static void insertDeliveries(Connection connection, int numProducts, int numCategories) throws SQLException {
//        String insertProductSQL = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (SELECT product_id FROM products, SELECT store_id FROM stores, ?)";
//        Random random = new Random();
//        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
//            for (int i = 0; i < numProducts; i++) {
//                int productID = random.nextInt(numCategories) + 1; // Categories are 1-indexed
//                preparedStatement.setInt(1, productName);
//                preparedStatement.setInt(2, categoryId);
//                preparedStatement.setInt(3,random.nextInt(1000));
//                preparedStatement.addBatch();
//            }
//            preparedStatement.executeBatch();
//        }
//    }
//    public static void test(){
//        String sqlString = "CREATE TABLE LogIn"
//        + "(user_name VARCHAR2(10),"
//        + " pass_word VARCHAR2(10),"
//        + " login_ID int )";
//        Statement stmt = con.createStatement();
//        stmt.execute(sqlString);
//    }
}
