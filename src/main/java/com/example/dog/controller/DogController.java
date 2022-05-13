package com.example.dog.controller;

import com.example.dog.model.Dog;
import com.example.dog.service.DogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
    private final DogService service;

    public DogController(DogService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        Dog dog = service.get(id);
        if (dog == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @PostMapping
    public Dog createDog(@Valid @RequestBody Dog dog) {
        return service.add(dog);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDog(@PathVariable("id") int id, @Valid @RequestBody Dog dog) {
        Dog addedDog = service.update(id, dog);
        return new ResponseEntity<>(addedDog, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        Dog dog = service.remove(id);
        if (dog == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }
}