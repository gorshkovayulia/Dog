package com.example.dog.controller;

import com.example.dog.model.Dog;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.*;

public class DogControllerRestAssuredTest {

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://localhost:8081";
    }

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));

        Dog fromPost = getDogFromPostRequest(dog);
        Dog fromGet = getDog(fromPost.getId());

        dog.setId(fromGet.getId());
        ReflectionAssert.assertReflectionEquals(dog, fromGet);
        ReflectionAssert.assertReflectionEquals(dog, fromPost);
    }

    @Test
    public void returns404_ifDogNotFoundDuringGet() {
        Response resp = getDogAndReturn(Integer.MAX_VALUE);
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }

    @Test
    public void possibleToUpdateExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 5, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog2 = new Dog("Sharik", 15, 10,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 6, 59),
                        ZoneId.of("Europe/Moscow")));

        Dog fromPost = getDogFromPostRequest(dog);
        Dog fromPut = getDogFromPutRequest(fromPost.getId(), dog2);
        dog2.setId(fromPost.getId());
        ReflectionAssert.assertReflectionEquals(dog2, fromPut);
    }

    @Test
    public void returns500_ifUpdatingNotExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 7, 59),
                        ZoneId.of("Europe/Moscow")));

        Response resp = updateDogAndReturn(Integer.MAX_VALUE, dog);
        Assert.assertEquals(resp.getStatusCode(), 400);
        Assert.assertEquals(resp.getBody().asString(), "\"" + "Dog with id=" + Integer.MAX_VALUE + " was not found!" + "\"");
    }

    @Test
    public void possibleToDeleteExistingDog() {
        Dog fromPost = getDogFromPostRequest(new Dog("Scooby-Doo", 80, 3,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 8, 59),
                        ZoneId.of("Europe/Moscow"))));

        Dog fromDelete = getDogFromDeleteRequest(fromPost.getId());
        ReflectionAssert.assertReflectionEquals(fromPost, fromDelete);
        Response resp = getDogAndReturn(fromDelete.getId());
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    @Test
    public void returns404_ifDeletingNotExistingDog() {
        Response resp = deleteDogAndReturn(Integer.MAX_VALUE);
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    private static Dog getDog(int id) {
        Response resp = getDogAndReturn(id);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static Response getDogAndReturn(int id) {
        return RestAssured.get("/dog/{id}", id).thenReturn();
    }

    private static Dog getDogFromPostRequest(Dog dog) {
        Response resp = postDogAndReturn(dog);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static Response postDogAndReturn(Dog dog) {
        return RestAssured.
                given().contentType("application/json").body(dog).
                when().post("/dog").thenReturn();
    }

    private static Dog getDogFromPutRequest(int id, Dog dog) {
        Response resp = updateDogAndReturn(id, dog);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static Response updateDogAndReturn(int id, Dog dog) {
        return RestAssured.
                given().contentType("application/json").body(dog).
                when().put("/dog/{id}", id).thenReturn();
    }

    private static Dog getDogFromDeleteRequest(int id) {
        Response resp = deleteDogAndReturn(id);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static Response deleteDogAndReturn(int id) {
        return RestAssured.delete("/dog/{id}", id).thenReturn();
    }
}
