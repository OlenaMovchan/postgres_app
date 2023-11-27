package com.shpp;


import com.github.javafaker.Faker;
import com.shpp.dto.Category;
import com.shpp.dto.Product;
import com.shpp.dto.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class DataInserting {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserting.class);
    private static final int NUMBER_OF_PRODUCT_NAMES = 40000;
    private static final int NUMBER_OF_CATEGORIES = 1000;
    public static final int NUMBER_OF_STORES = 75;
    private ValidatorClass validator = new ValidatorClass();
    private Faker faker = new Faker(new Locale("uk"));

    public void insertStores() {
        String insertStore = "INSERT INTO stores (location) VALUES (?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStore)) {
            Stream.generate(() -> new Store("Epicenter, " + faker.address().fullAddress()))
                    .limit(NUMBER_OF_STORES)
                    .forEach(store -> {
                        if (validator.validateDTO(store)) {
                            try {
                                preparedStatement.setString(1, store.getLocation());
                                preparedStatement.addBatch();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

            preparedStatement.executeBatch();
            LOGGER.info("Insertion of data by stores is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting stores", e.getMessage());
        }
    }

    public void insertProductCategories() {
        String insertCategorySQL = "INSERT INTO categories (category_name) VALUES (?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            Stream.generate(() -> new Category(faker.commerce().department()))
                    .limit(NUMBER_OF_CATEGORIES)
                    .forEach(categoryDTO -> {
                        if (validator.validateDTO(categoryDTO)) {
                            try {
                                preparedStatement.setString(1, categoryDTO.getCategoryName());
                                preparedStatement.addBatch();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            preparedStatement.executeBatch();
            LOGGER.info("Insertion of data by categories is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting categories", e.getMessage());
        }
    }

    public void insertProducts(String tableName) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        LOGGER.info("availableProcessor:  " + Runtime.getRuntime().availableProcessors());
        String insertQuery = "INSERT INTO " + tableName + " (product_name, category_id) VALUES (?, ?)";
        LOGGER.info("start inserting data into the table products");
        double start = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try (Connection connection = ConnectorDB.getConnection()) {
            for (int i = 0; i < 10; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeInsertProducts(insertQuery, connection), executorService);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            LOGGER.error("Error ", e.getMessage());
        } finally {
            executorService.shutdown();
        }
        LOGGER.info("end inserting data into the table products");
        double end = System.currentTimeMillis();
        double res = end - start;
        LOGGER.info("time " + res / 1000 + " s");
    }

    public void executeInsertProducts(String sql, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (int i = 0; i < 10000; i++) {
                Product product = new Product(faker.commerce().productName(),
                        faker.number().numberBetween(1, 1000));
                if (validator.validateDTO(product)) {
                    statement.setString(1, product.getProductName());
                    statement.setInt(2, product.getCategoryId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            connection.commit();
            LOGGER.info("Insertion of 10000 product data is successful");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertStoreProducts() {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        LOGGER.info("available processor:  " + Runtime.getRuntime().availableProcessors());
        String insertQuery = "INSERT INTO store_products (store_id, product_id, quantity) VALUES (?, ?, ?)";
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        LOGGER.info("start inserting data into the table store_products");
        double start = System.currentTimeMillis();
        try (Connection connection = ConnectorDB.getConnection()) {
            for (int storeId = 1; storeId <= NUMBER_OF_STORES; storeId++) {
                int finalStoreId = storeId;
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeInsertStoreProducts(insertQuery, connection, finalStoreId), executorService);
                futures.add(future);
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        LOGGER.info("end inserting data into the table store_products");
        double end = System.currentTimeMillis();
        double res = (end - start)/1000;
        LOGGER.info("time " + res + "s");
        LOGGER.info("The speed of data insertion per second: {}", 3000000 / res);//TODO prop
    }

    public void executeInsertStoreProducts(String sql, Connection connection, int storeId) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            //for (int storeId = 1; storeId <= NUMBER_OF_STORES; storeId++) {
                for (int productId = 1; productId <= NUMBER_OF_PRODUCT_NAMES; productId++) {
                    int randomQuantity = ThreadLocalRandom.current().nextInt(1, 1000);
                    statement.setInt(1, storeId);
                    statement.setInt(2, productId);
                    statement.setInt(3, randomQuantity);
                    statement.addBatch();
                    if (productId % 1000 == 0) {
                        statement.executeBatch();
                    }
                }
                LOGGER.info("Insertion of product data {} for the {} store is successful", NUMBER_OF_PRODUCT_NAMES, storeId);
           // }
            statement.executeBatch();
            LOGGER.info("Data inserted into store_products table successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



