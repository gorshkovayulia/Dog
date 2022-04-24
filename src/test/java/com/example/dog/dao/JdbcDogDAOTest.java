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

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@ContextConfiguration(locations={"classpath:/controllers-context.xml"})
public class JdbcDogDAOTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private JdbcDogDAO jdbcDogDao;

    @Test
    public void possibleToSaveNameWithMaxValues() {
        Dog dog = new Dog(
                "QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDFG",
                Integer.MAX_VALUE, Integer.MAX_VALUE, ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("Europe/Moscow")));
        assertReflectionEquals(jdbcDogDao.add(dog), dog);
    }

    @Test
    public void possibleToSaveDogWithSQLInjectedName() {
        Dog dog = new Dog("' blah", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        assertReflectionEquals(jdbcDogDao.add(dog), dog);
    }

    @Test
    public void possibleToSaveDogWithNullBirthday() {
        Dog dog = new Dog("Sharik", 24, 8, null);
        jdbcDogDao.add(dog);
        assertReflectionEquals(jdbcDogDao.get(dog.getId()), dog);
    }

    @Test
    public void nullIsReturnedInCaseGettingOfNotExistingDog() {
        Assert.assertNull(jdbcDogDao.get(Integer.MAX_VALUE));
    }

    @Test
    public void IllegalArgumentExceptionIsThrownInCaseUpdatingOfNotExistingDog() {
        Assert.assertThrows(IllegalArgumentException.class, () -> jdbcDogDao.update(Integer.MAX_VALUE, new Dog("Sharik", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")))));
    }

    @Test
    public void nullDogIsReturnedInCaseDeletingOfNotExistingDog() {
        Assert.assertNull(jdbcDogDao.remove(Integer.MAX_VALUE));
    }
}