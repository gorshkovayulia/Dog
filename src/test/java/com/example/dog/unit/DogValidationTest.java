package com.example.dog.unit;

import com.example.dog.Dog;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    public void possibleToSetCorrectName() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(0, violations.size());
    }

    @Test
    public void possibleToSetNameWithOneCharacter() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(0, violations.size());
    }

    @Test
    public void possibleToSetNameWithHundredCharacters() {
        Dog dog = new Dog("QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDF",
                24, 8, new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(0, violations.size());
    }

    @Test
    public void impossibleToSetNullName() {
        Dog dog = new Dog(null, 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 1 and 100 characters long.", violations.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetEmptyName() {
        Dog dog = new Dog("", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 1 and 100 characters long.", violations.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetNameWithMoreThanHundredCharacters() {
        Dog dog = new Dog("QWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERQWERTYUIOPASDFGHJKLZXCVBNMQWERTYUIOPASDFG",
                24, 8, new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("Name must be between 1 and 100 characters long.", violations.iterator().next().getMessage());
    }



    @Test
    public void possibleToSetCorrectHeight() {
        Dog dog = new Dog("Tuzik", 1, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(0, violations.size());
    }

    @Test
    public void impossibleToSetZeroHeight() {
        Dog dog = new Dog("Tuzik", 0, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("The height must be equal or greater than 0.", violations.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetNegativeHeight() {
        Dog dog = new Dog("Tuzik", -1, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("The height must be equal or greater than 0.", violations.iterator().next().getMessage());
    }



    @Test
    public void possibleToSetCorrectWeight() {
        Dog dog = new Dog("Tuzik", 500, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(0, violations.size());
    }

    @Test
    public void impossibleToSetZeroWeight() {
        Dog dog = new Dog("Tuzik", 0, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("The weight must be equal or greater than 0.", violations.iterator().next().getMessage());
    }

    @Test
    public void impossibleToSetNegativeWeight() {
        Dog dog = new Dog("Tuzik", -1, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Set<ConstraintViolation<Dog>> violations = validator.validate(dog);

        assertEquals(1, violations.size());
        assertEquals("The weight must be equal or greater than 0.", violations.iterator().next().getMessage());
    }
}