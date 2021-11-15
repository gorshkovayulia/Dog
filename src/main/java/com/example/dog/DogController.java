package com.example.dog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
    private static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();
    private static int id;

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        if (dogs.containsKey(id)) {
            return new ResponseEntity<>(dogs.get(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public Dog createDog(@RequestBody Dog dog) {
        id++;
        dog.setId(id);
        dogs.put(dog.getId(), dog);
        return dog;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
        if (dogs.containsKey(id)) {
            dogs.replace(id, dog);
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        if (dogs.containsKey(id)) {
            return new ResponseEntity<>(dogs.remove(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}