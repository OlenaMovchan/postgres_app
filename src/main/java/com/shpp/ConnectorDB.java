package com.shpp;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectorDB {
    public static Connection getConnection() throws SQLException {

        LoadingProperties properties = new LoadingProperties();
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String pass = properties.getProperty("db.password");

        return DriverManager.getConnection(url, user, pass);
    }

    public static void setDataSource(JdbcDataSource dataSource, String url) {
        dataSource.setURL(url);
    }
}
