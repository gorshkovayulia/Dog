package com.example.dog.controller;

import com.example.dog.dao.DogDAO;
import com.example.dog.dao.ObjectNotFoundException;
import com.example.dog.model.Dog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
    private final DogDAO dao;

    public DogController(DogDAO dao) {
        this.dao = dao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
        Dog dog = dao.get(id);
        if (dog == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }

    @PostMapping
    public Dog createDog(@Valid @RequestBody Dog dog) {
        return dao.add(dog);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateDog(@PathVariable("id") int id, @Valid @RequestBody Dog dog) {
        try {
            Dog addedDog = dao.update(id, dog);
            return new ResponseEntity<>(addedDog, HttpStatus.OK);
        } catch (ObjectNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
        Dog dog = dao.remove(id);
        if (dog == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(dog, HttpStatus.OK);
    }
}