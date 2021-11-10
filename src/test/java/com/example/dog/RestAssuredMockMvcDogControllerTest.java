package com.example.dog;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.springframework.http.HttpStatus;

import org.unitils.reflectionassert.ReflectionAssert;

import java.time.LocalDate;
import java.time.Month;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class RestAssuredMockMvcDogControllerTest {

    @BeforeClass
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new DogController());
    }

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog(1, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        given().contentType("application/json").body(dog)
                .when().post("/dog");

        MockMvcResponse resp = given().when().get("/dog/1");
        Dog dog2 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void impossibleToGetNotExistingDog() {
        given().when().get("/dog/100").
                then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void possibleToCreateDog() {
        Dog dog = new Dog(2, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        MockMvcResponse resp = given().contentType("application/json").body(dog)
                .when().post("/dog");

        Dog dog2 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void possibleToUpdateExistingDog() {
        Dog dog = new Dog(3, "Tuzikk", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        Dog dog2 = new Dog(4,"Sharik", 15, 10,
                LocalDate.of(2021, Month.OCTOBER, 25));

        given().contentType("application/json").body(dog)
                .when().post("/dog");

        given().contentType("application/json").body(dog2).
                when().put("/dog/3");

        MockMvcResponse resp = given().when().get("/dog/4");
        Dog dog3 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog2, dog3);
    }

    @Test
    public void impossibleToUpdateNotExistingDog() {
        Dog dog = new Dog(5, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        given().contentType("application/json").body(dog).
                when().put("dog/10").
                then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void possibleToDeleteExistingDog() {
        Dog dog = new Dog(5, "Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        given().contentType("application/json").body(dog)
                .when().post("/dog");

        MockMvcResponse resp = given().when().delete("/dog/5");;

        Dog dog2 = resp.getBody().as(Dog.class);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void impossibleToDeleteNotExistingDog() {
        given().when().delete("/dog/100").
                then().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
