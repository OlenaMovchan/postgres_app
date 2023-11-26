package com.shpp;

import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectorDBTest {

    @Test
    public void testGetProperties() {

        LoadingProperties properties = new LoadingProperties();
        assertNotNull(properties);

        assertNotNull(properties.getProperty("db.url"));
        assertNotNull(properties.getProperty("db.user"));
        assertNotNull(properties.getProperty("db.user"));
    }

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = ConnectorDB.getConnection();

        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }

    @Test
    void givenConnectionObject_whenExtractMetaData_thenGetDbURL() throws Exception {
        Connection connection = ConnectorDB.getConnection();
        String dbUrl = connection.getMetaData().getURL();

        assertEquals("jdbc:postgresql://database-1.cthx9ogfzrzo.eu-central-1.rds.amazonaws.com:5432/postgres", dbUrl);
    }

}
