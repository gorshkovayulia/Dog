package com.example.dog;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.LocalDate;
import java.time.Month;

public class RestAssuredDogControllerTest {

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog(1, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Response resp = RestAssured.get("http://localhost:8081/dog/1");

        Dog dog2 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void impossibleToGetNotExistingDog() {
        Response resp = RestAssured.get("http://localhost:8081/dog/100");
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }

    @Test
    public void possibleToCreateDog() {
        Dog dog = new Dog(2, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        Response resp = RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Dog dog2 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void possibleToUpdateExistingDog() {
        Dog dog = new Dog(3, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        Dog dog2 = new Dog(4,"Sharik", 15, 10,
                LocalDate.of(2021, Month.OCTOBER, 25));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        RestAssured.
                given().contentType("application/json").body(dog2).
                when().put("http://localhost:8081/dog/3");

        Response resp = RestAssured.get("http://localhost:8081/dog/3");

        Dog dog3 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog2, dog3);
    }

    @Test
    public void impossibleToUpdateNotExistingDog() {
        Dog dog = new Dog(6, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        Response resp = RestAssured.
                given().contentType("application/json").body(dog).
                when().put("http://localhost:8081/dog/6");
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }


    @Test
    public void possibleToDeleteExistingDog() {
        Dog dog = new Dog(6, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Response resp = RestAssured.delete("http://localhost:8081/dog/6");
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 200);
    }

    @Test
    public void impossibleToDeleteNotExistingDog() {
        Response resp = RestAssured.
                delete("http://localhost:8081/dog/100");
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }
}
