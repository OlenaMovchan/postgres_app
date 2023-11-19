package com.shpp;

import com.github.javafaker.Faker;
import com.shpp.dto.CategoryDTO;
import com.shpp.dto.DeliveryDTO;
import com.shpp.dto.ProductDTO;
import com.shpp.dto.StoreDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class DataInserting {
    private static final int NUMBER_OF_PRODUCT_NAMES = 3000000;
    private static final int NUMBER_OF_CATEGORIES = 1000;
    private static final int NUMBER_OF_STORES = 75;

    public void insertStores(Connection connection) throws SQLException {
        String insertStoreSQL = "INSERT INTO stores (location) VALUES (?)";
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker(new Locale("uk"));
        ValidatorClass serviceClass = new ValidatorClass();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertStoreSQL)) {
            Stream.generate(() -> new StoreDTO("Epicentre, " + faker.address().fullAddress()))
                    .limit(NUMBER_OF_STORES)
                    .forEach(storeDTO -> {
                        if (serviceClass.validateDTO(storeDTO)) {
                            try {
                                preparedStatement.setString(1, "Epicentre, " + faker.address().fullAddress());
                                preparedStatement.addBatch();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

            preparedStatement.executeBatch();
        }
    }

    public void insertProductCategories(Connection connection, int batchSize) throws SQLException {
        String insertCategorySQL = "INSERT INTO categories (category_name) VALUES (?)";
        AtomicInteger integer = new AtomicInteger(1);
        ValidatorClass serviceClass = new ValidatorClass();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            Stream.generate(() -> new CategoryDTO("Category_" + integer.get()))
                    .limit(NUMBER_OF_CATEGORIES)
                    .forEach(categoryDTO -> {
                        if (serviceClass.validateDTO(categoryDTO)) {
                            try {
                                preparedStatement.setString(1, categoryDTO.getCategoryName());
                                preparedStatement.addBatch();
                                integer.incrementAndGet();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            preparedStatement.executeBatch();
            // connection.commit();
        }
    }

    public void insertProducts(Connection connection) throws SQLException {
        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker();
        ValidatorClass serviceClass = new ValidatorClass();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            forkJoinPool.submit(() ->
                            Stream.generate(() -> new ProductDTO(
                                            "Product_" + integer.get(),
                                            faker.number().numberBetween(1, NUMBER_OF_CATEGORIES)
                                    ))
                                    .parallel()
                                    .limit(NUMBER_OF_PRODUCT_NAMES)
                                    .forEach(productDTO -> {//executorService.submit(() ->
                                        if (serviceClass.validateDTO(productDTO)) {
                                            try {
                                                preparedStatement.setString(1, productDTO.getProductName());
                                                preparedStatement.setInt(2, productDTO.getCategoryId());
                                                preparedStatement.addBatch();
                                                integer.incrementAndGet();
                                                if (integer.get() % 20000 == 0) {
                                                    preparedStatement.executeBatch();
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    })
                    //executorService.shutdown();
                    //executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            ).join();
            preparedStatement.executeBatch();
        }

    }

    public void insertDeliveries(Connection connection, int batchSize) throws SQLException {
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker();
        ValidatorClass serviceClass = new ValidatorClass();
        String insertProductSQL = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            forkJoinPool.submit(() -> Stream.generate(() -> new DeliveryDTO(faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                                    faker.number().numberBetween(1, NUMBER_OF_STORES),
                                    faker.number().numberBetween(1, batchSize)))
                            .parallel()
                            .limit(NUMBER_OF_PRODUCT_NAMES)
                            .forEach(deliveryDTO -> {// executorService.submit(()->
                                if (serviceClass.validateDTO(deliveryDTO)) {
                                    try {
                                        preparedStatement.setInt(1, deliveryDTO.getProductId());
                                        preparedStatement.setInt(2, deliveryDTO.getStoreId());
                                        preparedStatement.setInt(3, deliveryDTO.getProductCount());
                                        preparedStatement.addBatch();
                                        integer.incrementAndGet();
                                        if (integer.get() % 20000 == 0) {
                                            preparedStatement.executeBatch();//commit
                                        }
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            })//;
                    //executorService.shutdown();
                    //executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            ).join();
            preparedStatement.executeBatch();
        }
    }

}

//package net.javaguides.postgresql.tutorial;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.Arrays;
//import java.util.List;
//
//public class InsertMultipleRecordsExample {
//
//    private final String url = "jdbc:postgresql://localhost/myDB";
//    private final String user = "postgres";
//    private final String password = "root";
//
//    private static final String INSERT_USERS_SQL = "INSERT INTO users" +
//        "  (id, name, email, country, password) VALUES " +
//        " (?, ?, ?, ?, ?);";
//
//    /**
//     * insert multiple users
//     */
//    public void insertUsers(List < User > list) {
//        try (
//            Connection conn = DriverManager.getConnection(url, user, password); PreparedStatement statement = conn.prepareStatement(INSERT_USERS_SQL);) {
//            int count = 0;
//
//            for (User user: list) {
//                statement.setInt(1, user.getId());
//                statement.setString(2, user.getName());
//                statement.setString(3, user.getEmail());
//                statement.setString(4, user.getCountry());
//                statement.setString(5, user.getPassword());
//
//                statement.addBatch();
//                count++;
//                // execute every 100 rows or less
//                if (count % 100 == 0 || count == list.size()) {
//                    statement.executeBatch();
//                }
//            }
//        } catch (SQLException ex) {
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        InsertMultipleRecordsExample example = new InsertMultipleRecordsExample();
//        example.insertUsers(Arrays.asList(new User(2, "Ramesh", "ramesh@gmail.com", "India", "password123"),
//            new User(3, "John", "john@gmail.com", "US", "password123")));
//    }
//}
