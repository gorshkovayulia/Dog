package com.example.dog.controller;

import com.example.dog.model.Dog;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.util.NestedServletException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.testng.Assert.assertThrows;

@ContextConfiguration(locations={"classpath:/controllers-context.xml"})
public class DogControllerRestAssuredMockMvcTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private DogController dogController;

    @BeforeClass
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(dogController);
    }

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(LocalDateTime.of(2021, Month.OCTOBER, 26, 5, 59),
                ZoneId.of("Europe/Moscow")));

        Dog fromPost = getDogFromPostRequest(dog);
        Dog fromGet = getDog(fromPost.getId());

        dog.setId(fromGet.getId());
        ReflectionAssert.assertReflectionEquals(dog, fromGet);
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

        Dog fromPost = getDogFromPostRequest(dog);
        Dog fromPut = getDogFromPutRequest(fromPost.getId(), dog2);
        dog2.setId(fromPost.getId());
        ReflectionAssert.assertReflectionEquals(dog2, fromPut);
    }

    @Test
    public void returns400_ifUpdatingNotExistingDog() throws Throwable {
        Dog dog = new Dog("Tuzik", 24, 8,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 7, 59),
                        ZoneId.of("Europe/Moscow")));
        MockMvcResponse resp = updateDogAndReturn(Integer.MAX_VALUE, dog);
        Assert.assertEquals(resp.getStatusCode(), 400);
        Assert.assertEquals(resp.getBody().asString(), "Dog with id=" + Integer.MAX_VALUE + " was not found!");
    }

    @Test
    public void possibleToDeleteExistingDog() {
        Dog fromPost = getDogFromPostRequest(new Dog("Scooby-Doo", 80, 3,
                ZonedDateTime.of(
                        LocalDateTime.of(2021, Month.OCTOBER, 26, 8, 59),
                        ZoneId.of("Europe/Moscow"))));

        Dog fromDelete = getDogFromDeleteRequest(fromPost.getId());
        ReflectionAssert.assertReflectionEquals(fromPost, fromDelete);
        MockMvcResponse resp = getDogAndReturn(fromPost.getId());
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

        Dog fromPost = getDogFromPostRequest(dog);
        MockMvcResponse resp = updateDogAndReturn(fromPost.getId(), dog2);
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
