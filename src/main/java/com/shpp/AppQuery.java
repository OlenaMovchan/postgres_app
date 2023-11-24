package com.shpp;

import com.shpp.query.QueryExecutor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        String categoryName = System.getProperty("category", "Category_3");
        LOGGER.info("Start program");
        QueryExecutor queryExecutor = new QueryExecutor(categoryName);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        queryExecutor.querySQL();
        stopWatch.stop();
        LOGGER.info("Query time with indexes: " + stopWatch.getTime() + " ms");
    }
}
