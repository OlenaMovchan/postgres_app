package com.shpp;

import com.shpp.query.QueryExecutor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class QueryExecutorTest {
    static Connection connection;
    static Statement statementMock;
    static QueryExecutor executor;

    @BeforeAll
    static void init() throws SQLException {
        connection = mock(Connection.class);
        statementMock = mock(Statement.class);
        executor = mock(QueryExecutor.class);
        when(connection.createStatement()).thenReturn(statementMock);
    }

    @Test
    void executeSqlScriptTest() {
        //doCallRealMethod().when(executor)
               // .querySQL("CREATE TABLE categories ( category_id   SERIAL PRIMARY KEY, category_name VARCHAR(255) NOT NULL)");
       // executor.executeSqlScript(DDL_TEST_SCRIPT_FILE, DDL_TEST_SCRIPT_PATH, connection);
        //verify(executor).querySQL(DDL_TEST_SCRIPT_FILE, DDL_TEST_SCRIPT_PATH, connection);
    }

    @Test
    void executeSqlScriptThrowRuntimeException() {
       // doCallRealMethod().when(executor).querySQL("", "");
      //  assertThrows(RuntimeException.class,
               // () -> executor.querySQL("", ""));
    }
    @Test
    void testQuerySQL() throws SQLException {

        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        //QueryExecutor queryExecutor = new QueryExecutor();


        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("category_name")).thenReturn("Category_435");
        when(mockResultSet.getString("store_location")).thenReturn("MockStore");
        when(mockResultSet.getInt("total_products")).thenReturn(100);


        //doReturn(mockConnection).when(ConnectorDB.class, "getConnection");


        //queryExecutor.querySQL();

//
//        verify(mockConnection).prepareStatement(anyString());
//        verify(mockStatement).setString(1, "Category_435");
//        verify(mockStatement).executeQuery();

    }
}

