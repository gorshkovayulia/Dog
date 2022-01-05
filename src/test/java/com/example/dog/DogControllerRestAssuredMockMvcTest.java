package com.example.dog;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.unitils.reflectionassert.ReflectionAssert;

import java.time.*;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class DogControllerRestAssuredMockMvcTest {

    @BeforeClass
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new DogController());
    }

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 4, 59),
                        ZoneId.of("Europe/Moscow")));

        Dog fromPost = getDogFromPostRequest(dog);
        Dog fromDog = getDog(fromPost.getId());

        dog.setId(fromDog.getId());
        ReflectionAssert.assertReflectionEquals(dog, fromDog);
        ReflectionAssert.assertReflectionEquals(dog, fromPost);
    }

    @Test
    public void returns404_ifDogNotFoundDuringGet() {
        MockMvcResponse resp = getDogAndReturn(Integer.MAX_VALUE);
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

        Dog postDog = getDogFromPostRequest(dog);
        Dog putDog = getDogFromPutRequest(postDog.getId(), dog2);
        dog2.setId(postDog.getId());
        ReflectionAssert.assertReflectionEquals(dog2, putDog);
    }

    @Test
    public void returns404_ifUpdatingNotExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 7, 59),
                        ZoneId.of("Europe/Moscow")));

        MockMvcResponse resp = updateDogAndReturn(Integer.MAX_VALUE, dog);
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    @Test
    public void possibleToDeleteExistingDog() {
        Dog dog = getDogFromPostRequest(new Dog("Scooby-Doo", 80, 3,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 8, 59),
                        ZoneId.of("Europe/Moscow"))));

        Dog fromDelete = getDogFromDeleteRequest(dog.getId());
        ReflectionAssert.assertReflectionEquals(dog, fromDelete);
        MockMvcResponse resp = getDogAndReturn(dog.getId());
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    @Test
    public void returns404_ifDeletingNotExistingDog() {
        MockMvcResponse resp = deleteDogAndReturn(Integer.MAX_VALUE);
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    @Test
    public void returns400_ifPostingNotValidDog() {
        MockMvcResponse resp = postDogAndReturn(new Dog("", 80, 3,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 9, 59),
                        ZoneId.of("Europe/Moscow"))));
        Assert.assertEquals(resp.getStatusCode(), 400);
    }

    @Test
    public void returns400_ifUpdatingWithNotValidDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 10, 59),
                        ZoneId.of("Europe/Moscow")));
        Dog dog2 = new Dog("Sharik", 0, 10,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 11, 59),
                        ZoneId.of("Europe/Moscow")));

        Dog postDog = getDogFromPostRequest(dog);
        MockMvcResponse resp = updateDogAndReturn(postDog.getId(), dog2);
        Assert.assertEquals(resp.getStatusCode(), 400);
    }

    private static Dog getDog(int id) {
        MockMvcResponse resp = getDogAndReturn(id);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static MockMvcResponse getDogAndReturn(int id) {
        return given().when().get("/dog/{id}", id).thenReturn();
    }

    private static Dog getDogFromPostRequest(Dog dog) {
        MockMvcResponse resp = postDogAndReturn(dog);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static MockMvcResponse postDogAndReturn(Dog dog) {
        return given().contentType("application/json").body(dog).when().post("/dog");
    }

    private static Dog getDogFromPutRequest(int id, Dog dog) {
        MockMvcResponse resp = updateDogAndReturn(id, dog);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static MockMvcResponse updateDogAndReturn(int id, Dog dog) {
        return given().contentType("application/json").body(dog).
                when().put("/dog/{id}", id).thenReturn();
    }

    private static Dog getDogFromDeleteRequest(int id) {
        MockMvcResponse resp = deleteDogAndReturn(id);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static MockMvcResponse deleteDogAndReturn(int id) {
        return given().when().delete("/dog/{id}", id).thenReturn();
    }
}
