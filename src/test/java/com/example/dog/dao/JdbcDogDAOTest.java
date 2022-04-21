package com.example.dog.dao;

import com.example.dog.model.Dog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@ContextConfiguration(locations={"classpath:/controllers-context.xml"})
public class JdbcDogDAOTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcDogDAO jdbcDogDao;

    @Test
    public void possibleToSaveNameWithMaxLength() {
        Dog dog = new Dog(
                "QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDFG",
                24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Assert.assertEquals(jdbcDogDao.add(dog), dog);
    }

    @Test
    public void possibleToSaveDogWithSQLInjectedName() {
        Dog dog = new Dog("'a'='a'", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Assert.assertEquals(jdbcDogDao.add(dog), dog);
    }

    @Test
    public void possibleToSaveDogWithNullBirthday() {
        Dog dog = new Dog("Sharik", 24, 8, null);
        jdbcDogDao.add(dog);
        Assert.assertEquals(jdbcDogDao.get(dog.getId()), dog);
    }

    @Test
    public void nullIsReturnedInCaseGettingOfNotExistingDog() {
        Assert.assertNull(jdbcDogDao.get(Integer.MAX_VALUE));
    }

    @Test
    public void nullIsReturnedInCaseUpdatingOfNotExistingDog() {
        Assert.assertNull(jdbcDogDao.update(Integer.MAX_VALUE, new Dog("Sharik", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")))));
    }

    @Test
    public void nullDogIsReturnedInCaseDeletingOfNotExistingDog() {
        Assert.assertNull(jdbcDogDao.remove(Integer.MAX_VALUE));
    }
}