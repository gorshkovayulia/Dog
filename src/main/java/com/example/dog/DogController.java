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
        if(dogs.size() < id) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dogs.get(id), HttpStatus.OK);
    }

    @PostMapping
    public Dog createDog(@RequestBody Dog dog) {
        dogs.add(dog);
        return dog;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        if(dogs.size() < id) {
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dogs.set(id, dog), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        if(dogs.size() < id) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dogs.remove(id), HttpStatus.OK);
    }
}
