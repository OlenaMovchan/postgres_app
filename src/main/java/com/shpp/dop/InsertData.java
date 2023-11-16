package com.shpp.dop;

import com.github.javafaker.Faker;
import com.shpp.ServiceClass;
import com.shpp.dto.CategoryDTO;
import com.shpp.dto.DeliveryDTO;
import com.shpp.dto.ProductDTO;
import com.shpp.dto.StoreDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InsertData {
    private static final int NUMBER_OF_PRODUCT_NAMES = 3000000;
    private static final int NUMBER_OF_CATEGORIES = 1000;
    private static final int NUMBER_OF_STORES = 75;

    private static final String INSERT_STORE_QUERY = "INSERT INTO stores (location) VALUES (?)";
    private static final String INSERT_CATEGORY_QUERY = "INSERT INTO categories (category_name) VALUES (?)";
    private static final String INSERT_PRODUCT_QUERY = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
    private static final String INSERT_DELIVERY_QUERY = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";

    private static final Faker faker = new Faker(new Locale("uk"));
    private static final ServiceClass serviceClass = new ServiceClass();

    public static void insertStores(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_STORE_QUERY)) {
            Stream.generate(() -> new StoreDTO("Epicentre, " + faker.address().fullAddress()))
                    .limit(NUMBER_OF_STORES)
                    .filter(serviceClass::validateDTO)
                    .forEach(storeDTO -> addToBatch(preparedStatement, storeDTO.getLocation()));
            preparedStatement.executeBatch();
        }
    }

    public static void insertProductCategories(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORY_QUERY)) {
            IntStream.rangeClosed(1, NUMBER_OF_CATEGORIES)
                    .mapToObj(i -> new CategoryDTO("Category_" + i))
                    .filter(serviceClass::validateDTO)
                    .forEach(categoryDTO -> addToBatch(preparedStatement, categoryDTO.getCategoryName()));
            preparedStatement.executeBatch();
        }
    }

    public static void insertProducts(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT_QUERY)) {
            IntStream.rangeClosed(1, NUMBER_OF_PRODUCT_NAMES)
                    .mapToObj(i -> new ProductDTO("Product_" + i, faker.number().numberBetween(1, NUMBER_OF_CATEGORIES)))
                    .filter(serviceClass::validateDTO)
                    .forEach(productDTO -> addToBatch(preparedStatement, productDTO.getProductName(), productDTO.getCategoryId()));
            preparedStatement.executeBatch();
        }
    }

    public static void insertDeliveries(Connection connection) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DELIVERY_QUERY)) {
            IntStream.rangeClosed(1, NUMBER_OF_PRODUCT_NAMES)
                    .mapToObj(i -> new DeliveryDTO(
                            faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                            faker.number().numberBetween(1, NUMBER_OF_STORES),
                            faker.number().numberBetween(1, 1000)))
                    .filter(serviceClass::validateDTO)
                    .forEach(deliveryDTO -> addToBatch(
                            preparedStatement,
                            deliveryDTO.getProductId(),
                            deliveryDTO.getStoreId(),
                            deliveryDTO.getProductCount()));
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
