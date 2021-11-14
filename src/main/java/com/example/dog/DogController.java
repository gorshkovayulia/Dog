package com.example.dog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping(path="/dog", produces="application/json")
public class DogController {
//    private static ArrayList<Dog> dogs = new ArrayList<>();
    private static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();
    private int id;

    @GetMapping("/{id}")
    public ResponseEntity<Dog> getDog(@PathVariable("id") int id) {
//        for (Dog dog : dogs) {
//            if (dog.getId() == id) {
//                return new ResponseEntity<>(dog, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        for (Integer key : dogs.keySet()) {
            if (key == id) {
                return new ResponseEntity<>(dogs.get(key), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public Dog createDog(@RequestBody Dog dog) {
//        dogs.add(dog);
        id++;
        dog.setId(id);
        dogs.put(dog.getId(),dog);
        return dog;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Dog> updateDog(@PathVariable("id") int id, @RequestBody Dog dog) {
//        for (Dog dog2 : dogs) {
//            if (dog2.getId() == id) {
//                int idx = dogs.indexOf(dog2);
//                Dog oldDog = dogs.set(idx, dog);
//                dog.setId(oldDog.getId());
//                return new ResponseEntity<>(dog, HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        for (Integer key : dogs.keySet()) {
            if (key == id) {
                dogs.replace(key, dog);
                return new ResponseEntity<>(dog, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> removeDog(@PathVariable("id") int id) {
//        for (Dog dog : dogs) {
//            if (dog.getId() == id) {
//                int idx = dogs.indexOf(dog);
//                return new ResponseEntity<>(dogs.remove(idx), HttpStatus.OK);
//            }
//        }
//        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//    }
        for (Integer key : dogs.keySet()) {
            if (key == id) {
                return new ResponseEntity<>(dogs.remove(key), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}