package com.example.dog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
    private static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();
    private static AtomicInteger id = new AtomicInteger(0);

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        Dog dog = dogs.get(id);
        if (dog != null) {
            return new ResponseEntity<>(dog, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public Dog createDog(@Valid @RequestBody Dog dog) {
        id.incrementAndGet();
        dog.setId(id.get());
        dogs.put(dog.getId(), dog);
        return dog;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @Valid @RequestBody Dog dog) {
        Dog newValue = dogs.computeIfPresent(id, (k, v)-> dog);
        if(newValue != null) {
            newValue.setId(id);
            return new ResponseEntity<>(newValue, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        Dog deletedDog = dogs.remove(id);
        if (deletedDog != null) {
            return new ResponseEntity<>(deletedDog, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}