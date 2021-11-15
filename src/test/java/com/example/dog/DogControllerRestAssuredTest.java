package com.example.dog;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.LocalDate;
import java.time.Month;

public class DogControllerRestAssuredTest {

    @BeforeTest
    public void setup() {
        RestAssured.baseURI = "http://localhost:8081";
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
        Response resp = getDogAndReturn(Integer.MAX_VALUE);
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

        Response resp = updateDogAndReturn(Integer.MAX_VALUE, dog);
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

    private static Dog postDog(Dog dog) {
        Response resp = RestAssured.
                given().contentType("application/json").body(dog).
                when().post("/dog");
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
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
