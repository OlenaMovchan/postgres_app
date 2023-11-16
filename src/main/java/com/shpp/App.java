package com.shpp;

import java.sql.*;
import javax.sql.DataSource;

import com.shpp.query.QueryExecutor;
import org.apache.commons.lang3.time.StopWatch;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//commit?
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        int batchSize = 1000;
        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        DataInserting insertDataPreparedStatement = new DataInserting();
        QueryExecutor queryExecutor = new QueryExecutor();
        IndexCreator indexCreator = new IndexCreator();
        StopWatch stopWatch = new StopWatch();
        try (Connection connection = ConnectorDB.getConnection()) {
            databaseInitializer.createTable(connection);
            stopWatch.start();
            insertDataPreparedStatement.insertStores(connection);
            insertDataPreparedStatement.insertProductCategories(connection, batchSize);
            insertDataPreparedStatement.insertProducts(connection);
            insertDataPreparedStatement.insertDeliveries(connection, batchSize);
            LOGGER.info("Data generation completed");
            stopWatch.stop();
            LOGGER.info("Generation and insertion time: " + stopWatch.getTime() + " ms");
            stopWatch.reset();
            stopWatch.start();
            queryExecutor.querySQL(connection);
            stopWatch.stop();
            LOGGER.info("Query time without indexes: " + stopWatch.getTime() + " ms");

            indexCreator.createIndex(connection);
            stopWatch.reset();
            stopWatch.start();
            queryExecutor.querySQL(connection);
            stopWatch.stop();
            LOGGER.info("Query time with indexes: " + stopWatch.getTime() + " ms");
        } catch (SQLException e) {
            LOGGER.error("Error insert data", e.getMessage());
        }
    }


//        DataSource dataSource = createDataSource();
//       // Connection conn = dataSource.getConnection();
//
//        getAllBirds(conn);
//        getFilteredBirds(conn);
//        try {
//            insertBird(conn);
//        } catch (SQLException e) {
//            String errorCode = e.getSQLState();
//            // 08000 - connection_exception
//            if (errorCode == "08000") {
//                // retry query after re-establishing connection
//            }
//            // 42601 - syntax error
//            else if (errorCode == "42601") {
//                // throw error so that we can see the failure
//                throw e;
//            } else {
//                // log a warning, or do some other action based on the error code
//                System.out.printf("SQL failed with error code: %s%n", errorCode);
//            }
//
//        }
//        updateBird(conn);
//        deleteBird(conn);

    //}

    private static DataSource createDataSource() {
        final String url =
                "jdbc:postgresql://database-1.cthx9ogfzrzo.eu-central-1.rds.amazonaws.com:5432/postgres";
        final PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    // Getting all entries from database
    private static void getAllBirds(Connection conn) throws SQLException {
        // get a connection from the datasource
        // Create a new statement on the connection
        PreparedStatement stmt = conn.prepareStatement("select * from birds");
        // Execute the query, and store the results in the ResultSet instance
        ResultSet rs = stmt.executeQuery();
        // We run a loop to process the results.
        // The rs.next() method moves the result pointer to the next result row, and returns
        // true if a row is present, and false otherwise
        // note that initially the result pointer points before the first row, so we have to call
        // rs.next() the first time
        while (rs.next()) {
            // Now that `rs` points to a valid row (rs.next() is true), we can use the `getString`
            // and `getLong` methods to return each column value of the row as a string and long
            // respectively, and print it to the console
            System.out.printf("id:%d bird:%s description:%s%n", rs.getLong("id"),
                    rs.getString("bird"), rs.getString("description"));
        }
    }

    // Getting filtered entries from database using query params
    private static void getFilteredBirds(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from birds where bird=?");
        stmt.setString(1, "eagle");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.printf("id:%d bird:%s description:%s%n", rs.getLong("id"),
                    rs.getString("bird"), rs.getString("description"));
        }
    }

    // add a new bird to the table
    private static void insertBird(Connection conn) throws SQLException {
        PreparedStatement insertStmt =
                conn.prepareStatement("INSERT INTO birds(bird, description) VALUES (?, ?)");
        insertStmt.setString(1, "rooster");
        insertStmt.setString(2, "wakes you up in the morning");
        int insertedRows = insertStmt.executeUpdate();
        System.out.printf("inserted %s bird(s)%n", insertedRows);
    }

    // add a new bird to the table
    private static void updateBird(Connection conn) throws SQLException {
        // Similar to the previous example, we can use query params to fill in the condition
        // as well as the value to update
        PreparedStatement updateStmt =
                conn.prepareStatement("UPDATE birds SET description = ? WHERE bird = ?");
        updateStmt.setString(1, "has a red crown");
        updateStmt.setString(2, "rooster");
        int updatedRows = updateStmt.executeUpdate();
        System.out.printf("updated %s bird(s)%n", updatedRows);
    }

    // add a new bird to the table
    private static void deleteBird(Connection conn) throws SQLException {
        // Similar to the previous example, we can use query params to fill in the delete condition
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM birds WHERE bird = ?");
        deleteStmt.setString(1, "rooster");
        int deletedRows = deleteStmt.executeUpdate();
        System.out.printf("deleted %s bird(s)%n", deletedRows);
    }

}
//CREATE TABLE products
//(
//    product_id   SERIAL PRIMARY KEY,
//    store_id     INT,
//    product_name VARCHAR(255),
//    price        DECIMAL(10, 2),
//    category     VARCHAR(100)
//);

//CREATE TABLE accounts
//(
//    user_id    serial PRIMARY KEY,
//    username   VARCHAR(50) UNIQUE  NOT NULL,
//    password   VARCHAR(50)         NOT NULL,
//    email      VARCHAR(255) UNIQUE NOT NULL,
//    created_on TIMESTAMP           NOT NULL,
//    last_login TIMESTAMP
//);
//
//CREATE TABLE products
//(
//    product_id   SERIAL PRIMARY KEY,
//    store_id     INT,
//    product_name VARCHAR(255),
//    price        DECIMAL(10, 2),
//    category     VARCHAR(100)
//);
//CREATE TABLE birds
//(
//    bird        INT,
//    description VARCHAR(255)
//);
//CREATE TABLE Products (
//    Id INT PRIMARY KEY,
//    ProductName VARCHAR(30) NOT NULL,
//    Manufacturer VARCHAR(20) NOT NULL,
//    ProductCount INT DEFAULT 0,
//    Price MONEY NOT NULL
//);


//SELECT s.name AS store_name, s.location, COUNT(*) AS product_count
//FROM store s
//         JOIN product p ON s.store_id = p.store_id
//WHERE p.category_id = 434 -- Replace with the user's input
//GROUP BY s.name, s.location
//ORDER BY product_count DESC
//LIMIT 1;


//public class JdbcConnection {
//
//    private static final Logger LOGGER =
//        Logger.getLogger(JdbcConnection.class.getName());
//    private static Optional<Connection> connection = Optional.empty();
//
//    public static Optional<Connection> getConnection() {
//        if (connection.isEmpty()) {
//            String url = "jdbc:postgresql://localhost:5432/sampledb";
//            String user = "postgres";
//            String password = "postgres";
//
//            try {
//                connection = Optional.ofNullable(
//                    DriverManager.getConnection(url, user, password));
//            } catch (SQLException ex) {
//                LOGGER.log(Level.SEVERE, null, ex);
//            }
//        }
//
//        return connection;
//    }
//}

//CREATE TABLE products
//(
//    product_id   INT NOT NULL GENERATED ALWAYS AS IDENTITY ( START 1 INCREMENT 1),
//    product_name VARCHAR(255) NOT NULL,
//    category_id  BIGINT,
//    CONSTRAINT product_pkey PRIMARY KEY (product_id)
//    FOREIGN KEY (category_id) REFERENCES categories (category_id)
//);

////CREATE INDEX idx_categories_category_id ON categories (category_id);
//CREATE INDEX idx_products_category_id ON products (category_id);
//CREATE INDEX idx_deliveries_product_id ON deliveries (product_id);
//CREATE INDEX idx_deliveries_store_id ON deliveries (store_id);
//CREATE INDEX idx_stores_store_id ON stores (store_id);

//CREATE INDEX idx_deliveries_category_specific ON deliveries (product_id, store_id, product_count)
//WHERE product_id IN (SELECT product_id FROM products WHERE category_id = 'your_category_id');

//CREATE MATERIALIZED VIEW mv_category_products AS
//SELECT
//    c.category_name,
//    s.location AS store_location,
//    SUM(d.product_count) AS total_products
//FROM
//    categories c
//        JOIN
//    products p ON c.category_id = p.category_id
//        JOIN
//    deliveries d ON p.product_id = d.product_id
//        JOIN
//    stores s ON d.store_id = s.store_id
//GROUP BY
//    c.category_name, s.location;
//
//CREATE INDEX idx_mv_category_products ON mv_category_products (category_name, total_products);