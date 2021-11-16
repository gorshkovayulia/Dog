package com.example.dog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.*;
import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, isGetterVisibility = NONE)
public class Dog {

    private int id;

    @NotNull(message = "Name cannot be null")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters long")
    private String name;

    @Min(value = 1, message = "Height must be greater than 0")
    private int height;

    @Min(value = 1, message = "Weight must be greater than 0")
    private int weight;

    @Past(message = "Date of birth must be before NOW")
    private LocalDate dateOfBirth;

    public Dog() {}

    public Dog(String name, int height, int weight, LocalDate dateOfBirth) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
