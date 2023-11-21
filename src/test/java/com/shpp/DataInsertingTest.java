package com.shpp;



import com.shpp.dto.Store;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

class DataInsertingTest {

    @Mock
    private ValidatorClass validatorClass;

    @Mock
    private ConnectorDB connectorDB;

    @InjectMocks
    private DataInserting dataInserting;

    @Test
    void insertStoresSuccessfulInsertion() throws SQLException {
        // Arrange
        Connection connection = mock(Connection.class);
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connectorDB.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(validatorClass.validateDTO(any(Store.class))).thenReturn(true);

        dataInserting.insertStores();


        //verify(preparedStatement, times(DataInserting.NUMBER_OF_STORES)).addBatch();
        verify(preparedStatement).executeBatch();
        //verify(connection, times(DataInserting.NUMBER_OF_STORES)).setString(eq(1), anyString());
        verify(connection).close();
    }

    public void initTestData(){

    }


}
