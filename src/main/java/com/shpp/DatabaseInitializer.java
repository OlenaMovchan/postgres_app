package com.shpp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.sql.*;
import java.util.Objects;

public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitializer.class);
    private static final String SCRIPT_FILE = "createTables.sql";
    private static final String SCRIPT_DELIMITER = ";";

    public void createTables() {
        try (Connection connection = ConnectorDB.getConnection();
             Statement statement = connection.createStatement()) {

            executeScript(statement, readScriptFile());

            LOGGER.info("Tables created successfully");

        } catch (SQLException e) {
            LOGGER.error("Error creating tables", e);
        }
    }

    public void executeScript(Statement statement, String script) throws SQLException {
        String[] queries = script.split(SCRIPT_DELIMITER);
        for (String query : queries) {
            if (!query.trim().isEmpty()) {
                statement.executeUpdate(query.trim());
            }
        }
    }

    public String readScriptFile() {

        try (FileInputStream file = new FileInputStream(SCRIPT_FILE);
             BufferedReader reader = new BufferedReader(new InputStreamReader(file))) {

            StringBuilder script = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                script.append(line).append(System.lineSeparator());
            }
            return script.toString();

        } catch (IOException | NullPointerException e) {
            LOGGER.error("Error reading script file", e);
            return "";
        }
    }
}









