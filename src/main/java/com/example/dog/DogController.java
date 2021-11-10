package com.example.dog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
    private static ArrayList<Dog> dogs = new ArrayList<>();

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        for (Dog dog : dogs) {
            if (dog.getId() == id) {
                return new ResponseEntity<>(dog, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public Dog createDog(@RequestBody Dog dog) {
        dogs.add(dog);
        return dog;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        for (Dog dog2 : dogs) {
            if (dog2.getId() == id) {
                return new ResponseEntity<>(dogs.set(dogs.indexOf(dog2), dog), HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        for (Dog dog : dogs) {
            if (dog.getId() == id) {
                return new ResponseEntity<>(dogs.remove(dogs.indexOf(dog)), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
