package com.shpp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectorDBTest {
//    @InjectMocks
//    private ConnectorDB connectorDB;
//    @Mock private Connection mockConnection;
//    @Mock
//    private Statement mockStatement;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//    }
//
//    @Test
//    public void testMockDBConnection() throws Exception {
//        Mockito.when(mockConnection.createStatement()).thenReturn(mockStatement);
//        //Mockito.when(mockConnection.createStatement().executeUpdate(Mockito.any())).thenReturn(1);
//        //int value = connectorDB.executeQuery("");
//        //Assert.assertEquals(value, 1);
//        Mockito.verify(mockConnection.createStatement(), Mockito.times(1));
//    }
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
