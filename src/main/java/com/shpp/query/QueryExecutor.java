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

        String sql = null;
        try {
            sql = loadQueryFromFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static String loadQueryFromFile() throws IOException {
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

