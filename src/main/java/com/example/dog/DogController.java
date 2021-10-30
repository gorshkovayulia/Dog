package com.example.dog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MultivaluedHashMap;
import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping(path="/dog", produces="application/json", consumes = "application/json")
public class DogController {
    private static ArrayList<Dog> dogs = new ArrayList<>();

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        if(dogs.size() < id) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dogs.get(id), HttpStatus.OK);
    }

    @PostMapping(consumes="application/json", produces = "application/json")
    public ResponseEntity<Dog> createDog(@RequestBody Dog dog) {
        dogs.add(dog);
        MultivaluedHashMap<String, String> headers = new MultivaluedHashMap<>();
        headers.put("Content-Type", Arrays.asList("application/json"));
        return new ResponseEntity<Dog>(dog, headers, HttpStatus.CREATED);
//        return ResponseEntity.ok(dog);
    }

    @PutMapping(path = "/{id}", consumes="application/json")
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
