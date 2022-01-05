package com.example.dog;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.*;

import java.util.Set;

import static org.junit.Assert.*;

public class DogValidationTest {

    private static Validator validator;

    @BeforeClass
    public static void createValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void possibleToSetParametersWithMinValues() {
        Dog dog = new Dog("T", 1, 1, null);
        assertValid(dog);
    }

    @Test
    public void possibleToSetNameWithMaxLength() {
        Dog dog = new Dog(
                "QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDF",
                100, 100, ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4,
                        59), ZoneId.of("Europe/Moscow")));
        assertValid(dog);
    }

    @Test
    public void impossibleToSetNullOrEmptyOrMoreThanHundredLengthName() {
        Dog dog = new Dog(null, 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2021, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog2 = new Dog("", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2021, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog3 = new Dog(
                "QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDFG",
                24, 8,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);
        Set<ConstraintViolation<Dog>> violations2 = validator.validate(dog2);
        Set<ConstraintViolation<Dog>> violations3 = validator.validate(dog3);
        assertEquals(1, violations.size());
        assertEquals(1, violations2.size());
        assertEquals(1, violations3.size());
        assertEquals("Name cannot be null", violations.iterator().next().getMessage());
        assertEquals("Name must be between 1 and 100 characters long", violations2.iterator().next().getMessage());
        assertEquals("Name must be between 1 and 100 characters long", violations3.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetZeroOrNegativeHeight() {
        Dog dog = new Dog("Tuzik", 0, 10,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog2 = new Dog("Tuzik", -1, 10,
                ZonedDateTime.of(LocalDateTime.of(2020, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);
        Set<ConstraintViolation<Dog>> violations2 = validator.validate(dog2);
        assertEquals(1, violations.size());
        assertEquals(1, violations2.size());
        assertEquals("Height must be greater than 0, was 0", violations.iterator().next().getMessage());
        assertEquals("Height must be greater than 0, was -1", violations2.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetZeroOrNegativeWeight() {
        Dog dog = new Dog("Tuzik", 5, 0,
                ZonedDateTime.of(LocalDateTime.of(2021, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog2 = new Dog("Tuzik", 10, -1,
                ZonedDateTime.of(LocalDateTime.of(2021, Month.JANUARY, 1, 4, 59),
                        ZoneId.of("Europe/Moscow")));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);
        Set<ConstraintViolation<Dog>> violations2 = validator.validate(dog2);
        assertEquals(1, violations.size());
        assertEquals(1, violations2.size());
        assertEquals("Weight must be greater than 0, was 0", violations.iterator().next().getMessage());
        assertEquals("Weight must be greater than 0, was -1", violations2.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetBirthdayAsFutureDate() {
        ZonedDateTime time = ZonedDateTime.now().plusSeconds(10);
        Dog dog = new Dog("Tuzik", 15, 8, time);

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);
        assertEquals(1, violations.size());
        assertEquals("Date of birth must be before NOW, was " + time, violations.iterator().next().getMessage());
    }

    private void assertValid(Dog dog) {
        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);
        assertEquals(0, violations.size());
    }
}