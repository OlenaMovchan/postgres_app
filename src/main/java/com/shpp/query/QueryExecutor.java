package com.shpp.query;

import com.shpp.ConnectorDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class QueryExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryExecutor.class);
    private static final String QUERY_FILE = "query.sql";
    private final String categoryName;

    public QueryExecutor(String categoryName) {
        this.categoryName = categoryName;
    }

    public void querySQL() {
        String sql = loadQueryFromFile();
        QueryResult result;
        try {
            result = executeQuery(sql);
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

    public String loadQueryFromFile() {
        try (InputStream inputStream = getClass().getResourceAsStream(QUERY_FILE);//"/" +
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder query = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                query.append(line).append(System.lineSeparator());
            }
            return query.toString().replace("?", categoryName);

        } catch (IOException e) {
            throw new RuntimeException("Error reading query file", e);
        }
    }

    public QueryResult executeQuery(String sql) throws SQLException {
        try (Connection connection = ConnectorDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

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
