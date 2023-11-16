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
        ServiceClass serviceClass = new ServiceClass();
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
        ServiceClass serviceClass = new ServiceClass();
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

    public  void insertProducts(Connection connection) throws SQLException {
        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker();
        ServiceClass serviceClass = new ServiceClass();

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            Stream.generate(() -> new ProductDTO(
                            "Product_" + integer.get(),
                            faker.number().numberBetween(1, NUMBER_OF_CATEGORIES)
                    ))
                    .limit(NUMBER_OF_PRODUCT_NAMES)
                    .forEach(productDTO -> {
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
                    });
            preparedStatement.executeBatch();
        }

    }

    public void insertDeliveries(Connection connection, int batchSize) throws SQLException {
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker();
        ServiceClass serviceClass = new ServiceClass();
        String insertProductSQL = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            Stream.generate(() -> new DeliveryDTO(faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                    faker.number().numberBetween(1, NUMBER_OF_STORES),
                    faker.number().numberBetween(1, batchSize)))
                    .limit(NUMBER_OF_PRODUCT_NAMES)
                    .parallel()
                    .forEach(deliveryDTO -> {
                        if (serviceClass.validateDTO(deliveryDTO)) {
                            try {
                                preparedStatement.setInt(1, deliveryDTO.getProductId());
                                preparedStatement.setInt(2, deliveryDTO.getStoreId());
                                preparedStatement.setInt(3, deliveryDTO.getProductCount());
                                integer.incrementAndGet();
                                if (integer.get() % 20000 == 0) {
                                    preparedStatement.executeBatch();
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
            preparedStatement.executeBatch();
        }
    }

}
