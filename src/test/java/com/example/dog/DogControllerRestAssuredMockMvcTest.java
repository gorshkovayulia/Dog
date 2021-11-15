package com.example.dog;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import org.unitils.reflectionassert.ReflectionAssert;

import java.time.LocalDate;
import java.time.Month;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;

public class DogControllerRestAssuredMockMvcTest {

    @BeforeClass
    public void setUp() {
        RestAssuredMockMvc.standaloneSetup(new DogController());
    }

    @Test
    public void possibleToGetExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        Dog fromPost = postDog(dog);
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
                LocalDate.of(2021, Month.OCTOBER, 26));
        Dog dog2 = new Dog("Sharik", 15, 10,
                LocalDate.of(2020, Month.MARCH, 15));

        Dog postDog = postDog(dog);
        Dog putDog = getDogFromPutRequest(postDog.getId(), dog2);
        dog2.setId(putDog.getId());
        ReflectionAssert.assertReflectionEquals(dog2, putDog);
    }

    @Test
    public void returns404_ifUpdatingNotExistingDog() {
        Dog dog = new Dog("Tuzik", 24, 8,
                LocalDate.of(2021, Month.OCTOBER, 26));

        MockMvcResponse resp = updateDogAndReturn(Integer.MAX_VALUE, dog);
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    @Test
    public void possibleToDeleteExistingDog() {
        Dog dog = postDog(new Dog("Scooby-Doo", 80, 3,
                LocalDate.of(2019, Month.OCTOBER, 10)));

        Dog fromDelete = getDogFromDeleteRequest(dog.getId());
        ReflectionAssert.assertReflectionEquals(dog, fromDelete);
    }

    @Test
    public void returns404_ifDeletingNotExistingDog() {
        MockMvcResponse resp = deleteDogAndReturn(Integer.MAX_VALUE);
        Assert.assertEquals(resp.getStatusCode(), 404);
    }

    private static Dog getDog(int id) {
        MockMvcResponse resp = getDogAndReturn(id);
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private static MockMvcResponse getDogAndReturn(int id) {
        return given().when().get("/dog/{id}", id).thenReturn();
    }

    private static Dog postDog(Dog dog) {
        MockMvcResponse resp =
                given().contentType("application/json").body(dog)
                .when().post("/dog");
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
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
