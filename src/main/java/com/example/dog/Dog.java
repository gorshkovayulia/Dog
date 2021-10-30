package com.example.dog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.*;
import java.util.Calendar;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, isGetterVisibility = NONE)
public class Dog {
    @NotNull
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters long.")
    private String name;

    @NotNull
    @Min(value = 0, message = "Height must be equal or greater than 0.")
    private int height;

    @NotNull
    @Min(value = 0, message = "Weight must be equal or greater than 0.")
    private int weight;

    @Past(message = "Birthday must be before NOW.")
    private Calendar birthday;
    public Dog() {}
    public Dog(String name, int height, int weight, Calendar birthday) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.birthday = birthday;
    }

    public void play() {
        System.out.println("Bla bla");
    }
}
