package com.shpp.dop;

import com.github.javafaker.Faker;
import com.shpp.ConnectorDB;
import com.shpp.ValidatorClass;
import com.shpp.dto.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    static ValidatorClass validatorClass = new ValidatorClass();
static Faker faker = new Faker(new Locale("uk"));
    public void insertData() {

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.println("availableProcessor:  "+ Runtime.getRuntime().availableProcessors());
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
            try {
                // Execute each SQL statement asynchronously
                // for (String sql : sqlStatements) {
                for (int i = 0; i < 3000; i++) {
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeBulkInsert(insertQuery, connection), executorService);
                    futures.add(future);
                    // }
                }
                // Wait for all CompletableFuture objects to complete
                CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Shut down the executor service
                executorService.shutdown();

            }
        }catch (Exception e){
            e.printStackTrace();

        }

        System.out.println("end");
        double end = System.currentTimeMillis();
        double res = end - start;
        System.out.println("time " + res/1000 + "s");
    }

    private static void executeBulkInsert(String sql, Connection connection) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < 1000; i++) {
                Product product = new Product(faker.commerce().productName(),
                        faker.number().numberBetween(1, 1000));
                if (validatorClass.validateDTO(product)) {
                    statement.setString(1, product.getProductName());
                    statement.setInt(2, product.getCategoryId());
                    statement.addBatch();
                }
            }
            statement.executeBatch();
            //statement.executeUpdate();
            //System.out.println("Bulk insert executed: " + statement);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//public void save(List<Entity> entities) throws SQLException {
//    try (
//        Connection connection = database.getConnection();
//        PreparedStatement statement = connection.prepareStatement(SQL_INSERT);
//    ) {
//        int i = 0;
//
//        for (Entity entity : entities) {
//            statement.setString(1, entity.getSomeProperty());
//            // ...
//
//            statement.addBatch();
//            i++;
//
//            if (i % 1000 == 0 || i == entities.size()) {
//                statement.executeBatch(); // Execute every 1000 items.
//            }
//        }
//    }
//}