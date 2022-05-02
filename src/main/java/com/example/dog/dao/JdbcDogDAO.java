package com.example.dog.dao;

import com.example.dog.model.Dog;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.jdbc.support.JdbcUtils.closeConnection;

public class JdbcDogDAO implements DogDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss VV");
    private static final String SQL_SELECT_DOG =
            "select id, name, height, weight, birthday from dog where id = ?";
    private static final String SQL_INSERT_DOG =
            "insert into dog (name, height, weight, birthday) values (?, ?, ?, ?)";
    private static final String SQL_UPDATE_DOG =
            "update dog set name = ?, height = ?, weight = ?, birthday = ? where id = ?";
    private static final String SQL_DELETE_DOG =
            "delete from dog where id = ?";
    private final DriverManagerDataSource dataSource;

    public JdbcDogDAO(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Transactional(readOnly = true)
    @Override
    public Dog get(int id) {
        Connection conn;
        PreparedStatement statement;
        ResultSet resultSet;
        Dog returnedDog = null;
        try {
            conn = dataSource.getConnection();
            conn.setReadOnly(true);
            statement = conn.prepareStatement(SQL_SELECT_DOG);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                returnedDog = new Dog(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getInt("height"), resultSet.getInt("weight"),
                        getDateTime(resultSet.getTimestamp("birthday")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnedDog;
    }

    @Override
    public Dog add(Dog dog) {
        Connection conn = null;
        PreparedStatement statement;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(SQL_INSERT_DOG, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, dog.getName());
            statement.setInt(2, dog.getHeight());
            statement.setInt(3, dog.getWeight());
            statement.setString(4, getDateString(dog.getDateOfBirth()));
            statement.executeUpdate();
            conn.commit();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    dog.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            handleSQLException(conn, e);
        } finally {
            closeConnection(conn);
        }
        return dog;
    }

    @Override
    public Dog update(int id, Dog dog) {
        Connection conn = null;
        PreparedStatement statement;
        Dog updatedDog = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(SQL_UPDATE_DOG);
            statement.setString(1, dog.getName());
            statement.setInt(2, dog.getHeight());
            statement.setInt(3, dog.getWeight());
            statement.setString(4, getDateString(dog.getDateOfBirth()));
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            conn.commit();
            if (affectedRows == 0) {
                conn.rollback();
                throw new ObjectNotFoundException("Dog with id=" + id + " was not found!");
            }
            updatedDog = get(id);
        } catch (SQLException e) {
            handleSQLException(conn, e);
        } finally {
            closeConnection(conn);
        }
        return updatedDog;
    }

    @Override
    public Dog remove(int id) {
        Connection conn = null;
        PreparedStatement statement;
        Dog removedDog = get(id);
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            statement = conn.prepareStatement(SQL_DELETE_DOG);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            conn.commit();
            if (affectedRows == 0) {
                return null;
            }
        } catch (SQLException e) {
            handleSQLException(conn, e);
        } finally {
            closeConnection(conn);
        }
        return removedDog;
    }

    private static ZonedDateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
    }

    private String getDateString(ZonedDateTime time) {
        return time == null ? null : time.format(DATE_TIME_FORMATTER);
    }
    
    private void handleSQLException(Connection conn, SQLException e) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        throw new RuntimeException(e);
    }
}