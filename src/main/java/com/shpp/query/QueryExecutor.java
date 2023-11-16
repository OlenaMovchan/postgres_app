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
        +"c.category_name = Category_435 "
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

