package com.example.dog.controller;

import com.example.dog.dao.DogDAO;
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
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @Valid @RequestBody Dog dog) {
        Dog addedDog = dao.update(id, dog);
        if(addedDog == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(addedDog, HttpStatus.OK);
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