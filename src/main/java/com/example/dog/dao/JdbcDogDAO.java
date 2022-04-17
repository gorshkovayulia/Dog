package com.example.dog.dao;

import com.example.dog.model.Dog;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JdbcDogDAO implements DogDAO {

    private DriverManagerDataSource dataSource;

    public JdbcDogDAO(DriverManagerDataSource dataSource) {
        this.dataSource = dataSource;
        try (Connection conn = dataSource.getConnection()) {
            Statement statement = conn.createStatement();
            statement.execute("" +
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
        Statement statement;
        ResultSet resultSet;
        Dog returnedDog = null;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.createStatement();
            resultSet = statement.executeQuery("select id, name, height, weight, birthday from dog where id = " + id);
            if (resultSet.next()) {
                returnedDog = new Dog();
                returnedDog.setId(resultSet.getInt("id"));
                returnedDog.setName(resultSet.getString("name"));
                returnedDog.setHeight(resultSet.getInt("height"));
                returnedDog.setWeight(resultSet.getInt("weight"));
                returnedDog.setDateOfBirth(getDateTime(resultSet.getTimestamp("birthday")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return returnedDog;
    }

    @Override
    public Dog add(Dog dog) {
        Statement statement;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.createStatement();
            statement.executeUpdate("insert into dog (name, height, weight, birthday) values (" + "'" + dog.getName() + "', "
                            + dog.getHeight() + ", " + dog.getWeight() + "," + getDateString(dog.getDateOfBirth()) + ")",
                    Statement.RETURN_GENERATED_KEYS);

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
        Statement statement;
        Dog updatedDog;
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.createStatement();
            int affectedRows = statement.executeUpdate("update dog set name = " + "'" + dog.getName() + "'" +
                    ", height = " + dog.getHeight() + ", weight = " + dog.getWeight() +
                    ", birthday = " + getDateString(dog.getDateOfBirth()) + " where id = " + id);
            if (affectedRows == 0) {
                return null;
            }
            updatedDog = get(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return updatedDog;
    }

    @Override
    public Dog remove(int id) {
        Statement statement;
        Dog removedDog = get(id);
        try (Connection conn = dataSource.getConnection()) {
            statement = conn.createStatement();
            int affectedRows = statement.executeUpdate("delete from dog " + "where id = " + id);
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
        return time == null ? null : "'" + time.format(DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss VV")) + "'";
    }
}