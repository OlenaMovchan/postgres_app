package com.shpp;

import com.shpp.query.QueryExecutor;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AppQuery {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
    public static void main(String[] args) {
        QueryExecutor queryExecutor = new QueryExecutor();
        try (Connection connection = ConnectorDB.getConnection()) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            queryExecutor.querySQL(connection);
            stopWatch.stop();
            LOGGER.info("Time: " + stopWatch.getTime() + " ms");
        } catch (SQLException e) {

        }
    }
}
