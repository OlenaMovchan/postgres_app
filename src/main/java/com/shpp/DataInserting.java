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
    private static final int NUMBER_OF_STORES = 75;
    private static final int BATCH_SIZE = 1000;

    public void insertStores() {
        String insertStoreSQL = "INSERT INTO stores (location) VALUES (?)";
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker(new Locale("uk"));
        ValidatorClass serviceClass = new ValidatorClass();
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStoreSQL)) {
            Stream.generate(() -> new Store("Epicenter, " + faker.address().fullAddress()))
                    .limit(NUMBER_OF_STORES)
                    .forEach(store -> {
                        if (serviceClass.validateDTO(store)) {
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
        ValidatorClass serviceClass = new ValidatorClass();
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertCategorySQL)) {
            Stream.generate(() -> new Category("Category_" + integer.get()))
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
            LOGGER.info("Insertion of data by categories is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting categories", e.getMessage());
        }
    }

//    public void insertProducts() throws SQLException {
//        AtomicInteger count = new AtomicInteger(1);
//        ValidatorClass serviceClass = new ValidatorClass();
//        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
//        try (Connection connection = ConnectorDB.getConnection();
//                PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
//            IntStream.rangeClosed(1, NUMBER_OF_PRODUCT_NAMES)
//                    .mapToObj(i -> generateProduct(count))
//                    //.filter(ValidatorClass::validateDTO)
//                    .forEach(product -> {
//                        if (serviceClass.validateDTO(product)) {
//                            try {
//                                preparedStatement.setString(1, product.getProductName());
//                                preparedStatement.setInt(2, product.getCategoryId());
//                                preparedStatement.addBatch();
//                                if (count.incrementAndGet() % BATCH_SIZE == 0) {
//                                    preparedStatement.executeBatch();
//                                }
//                            } catch (SQLException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//
//            preparedStatement.executeBatch();
//        }
//    }
//
//    private static Product generateProduct(AtomicInteger count) {
//        Faker faker = new Faker();
//        String productName = "Product_" + count.get();
//        int categoryId =  faker.number().numberBetween(1, NUMBER_OF_CATEGORIES);
//
//        return new Product(productName, categoryId);
//    }
    public  void insertProducts() {
        String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";

        Faker faker = new Faker();
        ValidatorClass serviceClass = new ValidatorClass();

        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            //ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            AtomicInteger integer = new AtomicInteger(1);
            connection.setAutoCommit(false);
            System.out.println("Start stream");
            Stream.generate(() -> new Product(
                            "Product_" + integer.get(),
                            faker.number().numberBetween(1, NUMBER_OF_CATEGORIES)))
                    .limit(NUMBER_OF_PRODUCT_NAMES)
                    .forEach(product -> {
                        if (serviceClass.validateDTO(product)) {
                            try {
                                preparedStatement.setString(1, product.getProductName());
                                preparedStatement.setInt(2, product.getCategoryId());
                                preparedStatement.addBatch();
                                connection.commit();
                                integer.incrementAndGet();
                                if (integer.get() % 10000 == 0) {
                                    preparedStatement.executeBatch();
                                    preparedStatement.clearBatch();
                                }

                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            preparedStatement.executeBatch();
            connection.commit();
            LOGGER.info("Insertion of data by products is successful");
        } catch (SQLException e) {
            LOGGER.error("Error inserting products", e.getMessage());
        }

    }

    public void insertDeliveries(int batchSize) {
        AtomicInteger integer = new AtomicInteger(1);
        Faker faker = new Faker();
        ValidatorClass serviceClass = new ValidatorClass();
        String insertProductSQL = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
            Stream.generate(() -> new Delivery(faker.number().numberBetween(1, NUMBER_OF_PRODUCT_NAMES),
                    faker.number().numberBetween(1, NUMBER_OF_STORES),
                    faker.number().numberBetween(1, batchSize)))
                    .limit(NUMBER_OF_PRODUCT_NAMES)
                    .forEach(delivery -> {
                        if (serviceClass.validateDTO(delivery)) {
                            try {
                                preparedStatement.setInt(1, delivery.getProductId());
                                preparedStatement.setInt(2, delivery.getStoreId());
                                preparedStatement.setInt(3, delivery.getProductCount());
                                preparedStatement.addBatch();
                                integer.incrementAndGet();
                                if (integer.get() % 10000 == 0) {
                                    preparedStatement.executeBatch();//commit
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



