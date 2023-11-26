package com.shpp.dop;

import com.github.javafaker.Faker;
import com.shpp.App;
import com.shpp.ConnectorDB;
import com.shpp.ValidatorClass;
import com.shpp.dto.Delivery;
import com.shpp.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

// String insertProductSQL = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
//        try (Connection connection = ConnectorDB.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(insertProductSQL)) {
//            //AtomicInteger integer = new AtomicInteger(1);
//            for (int i = 0; i < NUMBER_OF_PRODUCT_NAMES; i++) {
//                Product product = new Product(faker.commerce().productName(),
//                        faker.number().numberBetween(1, NUMBER_OF_CATEGORIES));
//                if (validator.validateDTO(product)) {
//                    preparedStatement.setString(1, product.getProductName());
//                    preparedStatement.setInt(2, product.getCategoryId());
//                    preparedStatement.addBatch();
//                    //integer.incrementAndGet();
//                    if (i % 1000 == 0) {
//                        preparedStatement.executeBatch();
//                        preparedStatement.clearBatch();
//                    }
//                }
//            }
//            preparedStatement.executeBatch();
public class SQLTestLoadingBulk {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLTestLoadingBulk.class);
    static ValidatorClass validatorClass = new ValidatorClass();
    static Faker faker = new Faker(new Locale("uk"));

    public void insertData() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println("availableProcessor:  " + Runtime.getRuntime().availableProcessors());
        String insertQuery = "INSERT INTO products (product_name, category_id) VALUES (?, ?)";
        // Connection connection = ConnectorDB.getConnection();
        //Statement statement = connection.createStatement();
        //statement.executeUpdate("TRUNCATE TABLE [dbo].[test_data_DIAG]");
        //connection.close();


        List<String> sqlStatements = new ArrayList<>();
        //double start1 = System.currentTimeMillis();
        // for (int i = 0; i <3000; i++) {
        //sqlStatements.add(insertQuery);
        //}
        //double end1 = System.currentTimeMillis();
        //double res1 = end1 - start1;
        //System.out.println("time list  " + res1 + " ms");
        //sqlStatements.add(insertQuery);
        System.out.println("start");
        double start = System.currentTimeMillis();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        try
                (Connection connection = ConnectorDB.getConnection()) {

            // for (String sql : sqlStatements) {
            for (int i = 0; i < 4; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeBulkInsert(insertQuery, connection), executorService);
                futures.add(future);
                // }
            }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }


        System.out.println("end");
        double end = System.currentTimeMillis();
        double res = end - start;
        System.out.println("time " + res / 1000 + "s");
    }

    private static void executeBulkInsert(String sql, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            for (int i = 0; i < 10000; i++) {
                Product product = new Product(faker.commerce().productName(),
                        faker.number().numberBetween(1, 1000));
                if (validatorClass.validateDTO(product)) {
                    statement.setString(1, product.getProductName());
                    statement.setInt(2, product.getCategoryId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            LOGGER.info("Insertion of 10000 product data is successful");
            connection.commit();
            //statement.executeUpdate();
            //System.out.println("Bulk insert executed: " + statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertData2() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println("availableProcessor:  " + Runtime.getRuntime().availableProcessors());
        //String insertQuery = "INSERT INTO deliveries (product_id, store_id, product_count) VALUES (?, ?, ?)";
        String insertQuery = "INSERT INTO store_products (store_id, product_id, quantity) VALUES (?, ?, ?)";
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        System.out.println("start delivery insert ");
        double start = System.currentTimeMillis();
        try
                (Connection connection = ConnectorDB.getConnection()) {
           // for (int i = 0; i < 300; i++) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeBulkInsert2(insertQuery, connection), executorService);
                futures.add(future);

           // }
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
        System.out.println("end delivery insert");
        double end = System.currentTimeMillis();
        double res = end - start;
        System.out.println("time " + res / 1000 + "s");
    }

    private static void executeBulkInsert2(String sql, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            //connection.setAutoCommit(false);
            //for (int i = 0; i < 10000; i++) {
              //  Delivery delivery = new Delivery(faker.number().numberBetween(1, 500000),
                        //faker.number().numberBetween(1, 75),
                        //faker.number().numberBetween(1, 1000));
                //if (validatorClass.validateDTO(delivery)) {
                   // statement.setInt(1, delivery.getProductId());
                    //statement.setInt(2, delivery.getStoreId());
                   // statement.setInt(3, delivery.getProductCount());
                   // statement.addBatch();
                //}
            //}
            //statement.executeBatch();
            //statement.executeQuery();
            //connection.commit();
            int numberOfStores = 75;
            int numberOfProducts = 40000;

            // Insert random quantities for each combination of store and product
            for (int storeId = 1; storeId <= numberOfStores; storeId++) {
                for (int productId = 1; productId <= numberOfProducts; productId++) {
                    int randomQuantity = ThreadLocalRandom.current().nextInt(1, 1000); // Adjust the range as needed
                   Delivery delivery = new Delivery(productId, storeId, randomQuantity);
                    if (validatorClass.validateDTO(delivery)) {
                    statement.setInt(1, delivery.getStoreId());//1-store
                    statement.setInt(2, delivery.getProductId());
                    statement.setInt(3, delivery.getProductCount());

                    statement.addBatch();
                    }
                    if (productId%1000==0){
                        statement.executeBatch();
                    }
                }
                LOGGER.info("Insertion of product data {} for the {} store is successful", numberOfProducts, storeId);
            }

            // Execute the batch insert
            statement.executeBatch();

            System.out.println("Data inserted into store_products table successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
