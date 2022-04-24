package com.example.dog.dao;

import com.example.dog.model.Dog;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute(
                    "CREATE TABLE DOG (" +
                    "ID INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                    "NAME VARCHAR (255) NOT NULL, " +
                    "HEIGHT INTEGER NOT NULL, " +
                    "WEIGHT INTEGER NOT NULL, " +
                    "BIRTHDAY TIMESTAMP WITH TIME ZONE)");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Dog get(int id) {
        PreparedStatement statement;
        ResultSet resultSet;
        Dog returnedDog = null;
        try (Connection conn = dataSource.getConnection()) {
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
        PreparedStatement statement;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.prepareStatement(SQL_INSERT_DOG, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, dog.getName());
            statement.setInt(2, dog.getHeight());
            statement.setInt(3, dog.getWeight());
            statement.setString(4, getDateString(dog.getDateOfBirth()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    dog.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dog;
    }

    @Override
    public Dog update(int id, Dog dog) {
        PreparedStatement statement;
        Dog updatedDog;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.prepareStatement(SQL_UPDATE_DOG);
            statement.setString(1, dog.getName());
            statement.setInt(2, dog.getHeight());
            statement.setInt(3, dog.getWeight());
            statement.setString(4, getDateString(dog.getDateOfBirth()));
            statement.setInt(5, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new IllegalArgumentException("Dog with " + id + " id was not found!");
            }
            updatedDog = get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updatedDog;
    }

    @Override
    public Dog remove(int id) {
        PreparedStatement statement;
        Dog removedDog = get(id);
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.prepareStatement(SQL_DELETE_DOG);
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return removedDog;
    }

    private static ZonedDateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
    }

    private String getDateString(ZonedDateTime time) {
        return time == null ? null : time.format(DATE_TIME_FORMATTER);
    }
}