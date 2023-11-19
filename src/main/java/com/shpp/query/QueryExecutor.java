package com.shpp.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryExecutor.class);

    private static final String QUERY_FILE = "query.sql"; // Change this to the actual file name

    public  void querySQL(Connection connection) {
        String categoryName = System.getProperty("category", "Category_435");

        String sql = "SELECT " +
        "c.category_name, "
                +"s.location AS store_location, "
        +"SUM(d.product_count) AS total_products "
       +"FROM "
        +"categories c "
        +"JOIN "
        +"products p ON c.category_id = p.category_id "
        +"JOIN "
        +"deliveries d ON p.product_id = d.product_id "
        +"JOIN "
        +"stores s ON d.store_id = s.store_id "
        +"WHERE "
        +"c.category_name =? "
        +"GROUP BY "
        +"c.category_name, s.location "
        +"ORDER BY "
        +"total_products DESC "
        +"LIMIT 1";
        // sql = loadQueryFromFile();
        QueryResult result = null;
        try {
            result = executeQuery(connection, sql, categoryName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (result != null) {
            LOGGER.info("Category name: " + result.getCategoryName());
            LOGGER.info("Store location: " + result.getStoreLocation());
            LOGGER.info("Total products: " + result.getTotalProducts());
        } else {
            LOGGER.info("No results found for the given category");
        }
    }

    private String loadQueryFromFile() throws IOException {
        // String externalFilePath = "config/config.properties";
        //        try (FileInputStream input = new FileInputStream(externalFilePath)) {
        //            properties.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        StringBuilder queryBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(QueryExecutor.class.getClassLoader().getResourceAsStream("query.sql")))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queryBuilder.append(line).append("\n");
            }
        }
        return queryBuilder.toString();
    }

    private  QueryResult executeQuery(Connection connection, String sql, String categoryName) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String category = resultSet.getString("category_name");
                    String storeLocation = resultSet.getString("store_location");
                    int totalProducts = resultSet.getInt("total_products");
                    return new QueryResult(category, storeLocation, totalProducts);
                }
            }
        }
        return null;
    }

}
//package net.javaguides.postgresql.tutorial;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
///**
// * Select PreparedStatement JDBC Example
// *
// * @author Ramesh Fadatare
// *
// */
//public class RetrieveRecordsExample {
//
//    private final static String url = "jdbc:postgresql://localhost/mydb";
//    private final static String user = "postgres";
//    private final static String password = "root";
//
//    private static final String QUERY = "select id,name,email,country,password from Users where id =?";
//    private static final String SELECT_ALL_QUERY = "select * from users";
//
//    public void getUserById() {
//        // using try-with-resources to avoid closing resources (boiler plate
//        // code)
//
//        // Step 1: Establishing a Connection
//        try (Connection connection = DriverManager.getConnection(url, user, password);
//            // Step 2:Create a statement using connection object
//            PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {
//            preparedStatement.setInt(1, 1);
//            System.out.println(preparedStatement);
//            // Step 3: Execute the query or update query
//            ResultSet rs = preparedStatement.executeQuery();
//
//            // Step 4: Process the ResultSet object.
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String country = rs.getString("country");
//                String password = rs.getString("password");
//                System.out.println(id + "," + name + "," + email + "," + country + "," + password);
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }
//
//    public void getAllUsers() {
//        // using try-with-resources to avoid closing resources (boiler plate
//        // code)
//
//        // Step 1: Establishing a Connection
//        try (Connection connection = DriverManager.getConnection(url, user, password);
//            // Step 2:Create a statement using connection object
//            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_QUERY);) {
//            System.out.println(preparedStatement);
//            // Step 3: Execute the query or update query
//            ResultSet rs = preparedStatement.executeQuery();
//
//            // Step 4: Process the ResultSet object.
//            while (rs.next()) {
//                int id = rs.getInt("id");
//                String name = rs.getString("name");
//                String email = rs.getString("email");
//                String country = rs.getString("country");
//                String password = rs.getString("password");
//                System.out.println(id + "," + name + "," + email + "," + country + "," + password);
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//        }
//    }
//
//    public static void main(String[] args) {
//        RetrieveRecordsExample example = new RetrieveRecordsExample();
//        example.getUserById();
//        example.getAllUsers();
//    }
//
//    public static void printSQLException(SQLException ex) {
//        for (Throwable e: ex) {
//            if (e instanceof SQLException) {
//                e.printStackTrace(System.err);
//                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
//                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
//                System.err.println("Message: " + e.getMessage());
//                Throwable t = ex.getCause();
//                while (t != null) {
//                    System.out.println("Cause: " + t);
//                    t = t.getCause();
//                }
//            }
//        }
//    }
//}

