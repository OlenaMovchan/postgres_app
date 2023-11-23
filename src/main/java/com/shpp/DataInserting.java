package com.shpp;


import com.github.javafaker.Faker;
import com.shpp.dto.Category;
import com.shpp.dto.Delivery;
import com.shpp.dto.Product;
import com.shpp.dto.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DataInserting {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataInserting.class);
    private static final int NUMBER_OF_PRODUCT_NAMES = 3000000;
    private static final int NUMBER_OF_CATEGORIES = 1000;
    public static final int NUMBER_OF_STORES = 75;
    private ValidatorClass validator = new ValidatorClass();
    private Faker faker = new Faker(new Locale("uk"));

    public void insertStores() {
        String insertStoreSQL = "INSERT INTO stores (location) VALUES (?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStoreSQL)) {
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
        AtomicInteger integer = new AtomicInteger(1);
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            Stream.generate(() -> new Category("Category_" + integer.get()))
                    .limit(NUMBER_OF_CATEGORIES)
                    .forEach(categoryDTO -> {
                        if (validator.validateDTO(categoryDTO)) {
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
            LOGGER.info("Insertion of data by categories is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting categories", e.getMessage());
        }
    }

    public void insertProducts() {
        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            AtomicInteger integer = new AtomicInteger(1);
            for (int i = 0; i < NUMBER_OF_PRODUCT_NAMES; i++) {
                Product product = new Product("Product_" + integer.get(),
                        faker.number().numberBetween(1, NUMBER_OF_CATEGORIES));
                if (validator.validateDTO(product)) {
                    preparedStatement.setString(1, product.getProductName());
                    preparedStatement.setInt(2, product.getCategoryId());
                    preparedStatement.addBatch();
                    integer.incrementAndGet();
                    if (integer.get() % 10000 == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }
                }
            }
            preparedStatement.executeBatch();
            LOGGER.info("Insertion of data by products is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting products", e.getMessage());
        }
    }

    public void insertDeliveries(int batchSize) {
        AtomicInteger integer = new AtomicInteger(1);
        String insertProductSQL = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            Stream.generate(() -> new Delivery(faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                            faker.number().numberBetween(1, NUMBER_OF_STORES),
                            faker.number().numberBetween(1, batchSize)))
                    .limit(NUMBER_OF_PRODUCT_NAMES)
                    .forEach(delivery -> {
                        if (validator.validateDTO(delivery)) {
                            try {
                                preparedStatement.setInt(1, delivery.getProductId());
                                preparedStatement.setInt(2, delivery.getStoreId());
                                preparedStatement.setInt(3, delivery.getProductCount());
                                preparedStatement.addBatch();
                                integer.incrementAndGet();
                                if (integer.get() % 10000 == 0) {
                                    preparedStatement.executeBatch();
                                    preparedStatement.clearBatch();
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            preparedStatement.executeBatch();
            LOGGER.info("Insertion of data by deliveries is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting deliveries", e.getMessage());
        }
    }
}



