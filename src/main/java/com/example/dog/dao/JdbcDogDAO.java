package com.example.dog.dao;

import com.example.dog.model.Dog;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class JdbcDogDAO implements DogDAO {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss VV");
    private static final String SQL_SELECT_DOG = "select id, name, height, weight, birthday from dog where id = ?";
    private static final String SQL_INSERT_DOG = "insert into dog (name, height, weight, birthday) values (?, ?, ?, ?)";
    private static final String SQL_UPDATE_DOG = "update dog set name = ?, height = ?, weight = ?, birthday = ? where id = ?";
    private static final String SQL_DELETE_DOG = "delete from dog where id = ?";
    private final JdbcTemplate jdbcTemplate;

    public JdbcDogDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Dog get(int id) {
        try {
            return jdbcTemplate.queryForObject(SQL_SELECT_DOG, new Object[]{id}, (rs, rowNum) ->
                    new Dog(rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("height"),
                            rs.getInt("weight"),
                            getDateTime(rs.getTimestamp("birthday"))));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Dog add(Dog dog) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement statement = conn.prepareStatement(SQL_INSERT_DOG, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, dog.getName());
            statement.setInt(2, dog.getHeight());
            statement.setInt(3, dog.getWeight());
            statement.setString(4, getDateString(dog.getDateOfBirth()));
            return statement;
        }, keyHolder);
        dog.setId(keyHolder.getKey().intValue());
        return dog;
    }

    @Override
    public Dog update(int id, Dog dog) {
        Dog updatedDog;
        int affectedRows = jdbcTemplate.update(SQL_UPDATE_DOG, dog.getName(), dog.getHeight(), dog.getWeight(),
                getDateString(dog.getDateOfBirth()),id);
        if (affectedRows == 0) {
            throw new ObjectNotFoundException("Dog with id=" + id + " was not found!");
        }
        updatedDog = get(id);
        return updatedDog;
    }

    @Override
    public Dog remove(int id) {
        Dog removedDog = get(id);
        int affectedRows = jdbcTemplate.update(SQL_DELETE_DOG, id);
        return affectedRows == 0 ? null : removedDog;
    }

    private static ZonedDateTime getDateTime(Timestamp timestamp) {
        return timestamp != null ? ZonedDateTime.ofInstant(timestamp.toInstant(), ZoneId.systemDefault()) : null;
    }

    private String getDateString(ZonedDateTime time) {
        return time == null ? null : time.format(DATE_TIME_FORMATTER);
    }
}