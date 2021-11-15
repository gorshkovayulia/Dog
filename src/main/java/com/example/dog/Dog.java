package com.example.dog;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

//@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = NONE, isGetterVisibility = NONE)
@JsonAutoDetect(fieldVisibility = ANY, getterVisibility = ANY, isGetterVisibility = ANY)
public class Dog {

    private int id;

    @NotNull
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters long")
    private String name;

    @NotNull
    @Min(value = 0, message = "The height must be greater than 0")
    private int height;

    @NotNull
    @Min(value = 0, message = "The weight must be greater than 0")
    private int weight;

    @Past(message = "Birthday must be before NOW.")
    private LocalDate birthday;

    public Dog() {}

    public Dog(String name, int height, int weight, LocalDate birthday) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
