package com.example.dog.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Was replaced with {@link org.springframework.jdbc.datasource.DataSourceUtils}
 */
public class JdbcConnectionHolder {
    private final ThreadLocal<Connection> connections = new ThreadLocal<>();
    private final DataSource dataSource;

    public JdbcConnectionHolder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        Connection conn = connections.get();
        if (conn == null) {
            try {
                conn = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connections.set(conn);
        }
        return conn;
    }

    public void startTransaction() {
        Connection conn = getConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            close();
            throw new RuntimeException(e);
        }
    }

    public void commit() {
        try {
            getConnection().commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        try {
            getConnection().rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        Connection conn = connections.get();
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        connections.remove();
    }

    public void setReadOnly(boolean condition) {
        try {
            getConnection().setReadOnly(condition);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
