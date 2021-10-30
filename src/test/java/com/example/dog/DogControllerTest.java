package com.example.dog;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static io.restassured.RestAssured.given;

public class DogControllerTest {

    @Test
    public void givenId_whenDog_thenStatus200() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        given().contentType("application/json").body(dog)
                .when().post("/dog");
        given().pathParam("id", 1)
                .when().get("/dog/{id}")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenNoTExistingDog_whenGetDog_thenStatus404() {
        given().pathParam("id", 1)
                .when().get("/dog/{id}")
                .then().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void whenCreateDog_thenStatus201() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        given().contentType("application/json").body(dog)
                .when().post("/dog")
                .then().statusCode(HttpStatus.CREATED.value());
    }

    @Test
    public void givenDog_whenDog_thenStatus204() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Dog dog2 = new Dog("Sharik", 15, 10,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 25));

        given().contentType("application/json").body(dog)
                .when().post("/dog");
        given().pathParam("id",  1).contentType("application/json").body(dog2)
                .when().put("/dog")
                .then().statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    public void givenDog_whenDog_thenStatus404() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        Dog dog2 = new Dog("Sharik", 15, 10,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 25));

        given().contentType("application/json").body(dog)
                .when().post("/dog");
        given().pathParam("id",  2).contentType("application/json").body(dog2)
                .when().put("/dog")
                .then().statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void givenDog_whenDeleteDog_thenStatus200() {
        Dog dog = new Dog("Tuzik", 24, 8,
                new GregorianCalendar(2021, Calendar.NOVEMBER, 26));

        given().contentType("application/json").body(dog)
                .when().post("/dog");
        given().pathParam("id",  1).contentType("application/json")
                .when().delete("/dog/{id}")
                .then().statusCode(HttpStatus.OK.value());
    }

    @Test
    public void givenID_whenDeleteDog_thenStatus404() {
        given().pathParam("id",  1).contentType("application/json")
                .when().delete("/dog/{id}")
                .then().assertThat().statusCode(HttpStatus.NOT_FOUND.value());
    }
}
