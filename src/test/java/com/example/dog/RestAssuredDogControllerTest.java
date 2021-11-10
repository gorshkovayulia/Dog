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
        Dog dog = new Dog(11, "Tuzik", 24, 8, LocalDate.of(2021, Month.OCTOBER, 26));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Dog dog2 = getDogFromGetRequest(11);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void impossibleToGetNotExistingDog() {
        Response resp = getResponseFromGetRequest(100);
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }

    @Test
    public void possibleToCreateDog() {
        Dog dog = new Dog(12, "Tuzik", 24, 8, LocalDate.of(2021, Month.OCTOBER, 26));

        Dog dog2 = getDogFromPostRequest(dog);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void possibleToUpdateExistingDog() {
        Dog dog = new Dog(13, "Tuzik", 24, 8, LocalDate.of(2021, Month.OCTOBER, 26));
        Dog dog2 = new Dog(14,"Sharik", 15, 10, LocalDate.of(2021, Month.OCTOBER, 25));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Response resp = getResponseFromUpdateRequest(13, dog2);
        Dog dog3 = getDogFromGetRequest(14);
        ReflectionAssert.assertReflectionEquals(dog2, dog3);
    }

    @Test
    public void impossibleToUpdateNotExistingDog() {
        Dog dog = new Dog(20, "Tuzik", 24, 8, LocalDate.of(2021, Month.OCTOBER, 26));

        Response resp = getResponseFromUpdateRequest(50, dog);
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }


    @Test
    public void possibleToDeleteExistingDog() {
        Dog dog = new Dog(6, "Tuzik", 24, 8, LocalDate.of(2021, Month.OCTOBER, 26));

        RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");

        Dog dog2 = getDogFromDeleteRequest(6);
        ReflectionAssert.assertReflectionEquals(dog, dog2);
    }

    @Test
    public void impossibleToDeleteNotExistingDog() {
        Response resp = getResponseFromDeleteRequest(100);
        int code = resp.getStatusCode();
        Assert.assertEquals(code, 404);
    }

    private Dog getDogFromGetRequest(int id) {
        Response resp = RestAssured.get("http://localhost:8081/dog/{id}", id).thenReturn();
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private Dog getDogFromPostRequest(Dog dog) {
        Response resp = RestAssured.
                given().contentType("application/json").body(dog).
                when().post("http://localhost:8081/dog");
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private Dog getDogFromPutRequest(Dog dog, int id) {
        Response resp = RestAssured.
                given().contentType("application/json").body(dog).
                when().put("http://localhost:8081/dog/{id}", id);
        Assert.assertEquals(resp.getStatusCode(), 204);
        return resp.as(Dog.class);
    }

    private Dog getDogFromDeleteRequest(int id) {
        Response resp = RestAssured.delete("http://localhost:8081/dog/{id}", id).thenReturn();
        Assert.assertEquals(resp.getStatusCode(), 200);
        return resp.as(Dog.class);
    }

    private Response getResponseFromGetRequest(int id) {
        return RestAssured.get("http://localhost:8081/dog/{id}", id).thenReturn();
    }

    private Response getResponseFromUpdateRequest(int id, Dog dog) {
        return RestAssured.
                given().contentType("application/json").body(dog).
                when().put("http://localhost:8081/dog/{id}", id);
    }

    private Response getResponseFromDeleteRequest(int id) {
        return RestAssured.delete("http://localhost:8081/dog/{id}", id).thenReturn();
    }
}
