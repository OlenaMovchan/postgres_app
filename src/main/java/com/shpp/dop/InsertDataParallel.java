package com.shpp.dop;
import com.github.javafaker.Faker;
import com.shpp.ValidatorClass;
import com.shpp.dto.Category;
import com.shpp.dto.Delivery;
import com.shpp.dto.Product;
import com.shpp.dto.Store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class InsertDataParallel {


    private static final int NUMBER_OF_PRODUCT_NAMES = 3000000;
    private static final int NUMBER_OF_CATEGORIES = 1000;
    private static final int NUMBER_OF_STORES = 75;

    private static final String INSERT_STORE_QUERY = "INSERT INTO stores (location) VALUES (?)";
    private static final String INSERT_CATEGORY_QUERY = "INSERT INTO categories (category_name) VALUES (?)";
    private static final String INSERT_PRODUCT_QUERY = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
    private static final String INSERT_DELIVERY_QUERY = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";

    private static final Faker faker = new Faker(new Locale("uk"));
    private static final ValidatorClass serviceClass = new ValidatorClass();
    public static void insertStores(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STORE_QUERY)) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            forkJoinPool.submit(() ->
                    IntStream.range(0, NUMBER_OF_STORES)
                            .parallel()
                            .mapToObj(i -> new Store("Epicentre, " + faker.address().fullAddress()))
                            .filter(serviceClass::validateDTO)
                            .forEach(storeDTO -> addToBatch(preparedStatement, storeDTO.getLocation()))
            ).join();

            preparedStatement.executeBatch();
        }
    }

    public static void insertProductCategories(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_QUERY)) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            forkJoinPool.submit(() ->
                    IntStream.rangeClosed(1, NUMBER_OF_CATEGORIES)
                            .parallel()
                            .mapToObj(i -> new Category("Category_" + i))
                            .filter(serviceClass::validateDTO)
                            .forEach(categoryDTO -> addToBatch(preparedStatement, categoryDTO.getCategoryName()))
            ).join();

            preparedStatement.executeBatch();
        }
    }

    public static void insertProducts(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_QUERY)) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            forkJoinPool.submit(() ->
                    IntStream.rangeClosed(1, NUMBER_OF_PRODUCT_NAMES)
                            .parallel()
                            .mapToObj(i -> new Product("Product_" + i, faker.number().numberBetween(1, NUMBER_OF_CATEGORIES)))
                            .filter(serviceClass::validateDTO)
                            .forEach(productDTO -> addToBatch(preparedStatement, productDTO.getProductName(), productDTO.getCategoryId()))
            ).join();

            preparedStatement.executeBatch();
        }
    }

    public static void insertDeliveries(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DELIVERY_QUERY)) {
            ForkJoinPool forkJoinPool = new ForkJoinPool();
            forkJoinPool.submit(() ->
                    IntStream.rangeClosed(1, NUMBER_OF_PRODUCT_NAMES)
                            .parallel()
                            .mapToObj(i -> new Delivery(
                                    faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                                    faker.number().numberBetween(1, NUMBER_OF_STORES),
                                    faker.number().numberBetween(1, 1000)))
                            .filter(serviceClass::validateDTO)
                            .forEach(deliveryDTO -> addToBatch(
                                    preparedStatement,
                                    deliveryDTO.getProductId(),
                                    deliveryDTO.getStoreId(),
                                    deliveryDTO.getProductCount()))
            ).join();

            preparedStatement.executeBatch();
        }
    }

    private static void addToBatch(PreparedStatement preparedStatement, Object... params) {
        try {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

