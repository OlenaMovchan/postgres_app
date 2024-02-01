package com.shpp;

import com.shpp.query.QueryExecutor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        String categoryName = System.getProperty("category", "Дім");
        LOGGER.info("Start program");
        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        DataInserting insertDataPreparedStatement = new DataInserting();
        QueryExecutor queryExecutor = new QueryExecutor(categoryName);
        IndexCreator indexCreator = new IndexCreator();
        StopWatch stopWatch = new StopWatch();
        String script = databaseInitializer.readScriptFile();
        databaseInitializer.createTables(script);

        stopWatch.start();
        insertDataPreparedStatement.insertStores();
        insertDataPreparedStatement.insertProductCategories();
        insertDataPreparedStatement.insertProducts("products");
        insertDataPreparedStatement.insertStoreProducts();

        LOGGER.info("Data generation completed");

        stopWatch.stop();
        LOGGER.info("Generation and insertion time: " + stopWatch.getTime() + " ms");

        stopWatch.reset();
        stopWatch.start();
        queryExecutor.querySQL();
        stopWatch.stop();
        LOGGER.info("Query time without indexes: " + stopWatch.getTime() + " ms");

        indexCreator.createIndex();
        stopWatch.reset();
        stopWatch.start();
        queryExecutor.querySQL();
        stopWatch.stop();
        LOGGER.info("Query time with indexes: " + stopWatch.getTime() + " ms");

    }

}
