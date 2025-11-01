package org.example.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnectionPool {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/restaurant_db";
    private static final String DB_USER = "app";
    private static final String DB_PASSWORD = "app";

    private final HikariConfig config;
    private final HikariDataSource dataSource;

    public DBConnectionPool() {
        this.config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        this.dataSource = new HikariDataSource(this.config);
    }

    public Connection createConnection() throws SQLException {

        return this.dataSource.getConnection();
    }

}
