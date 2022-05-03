package com.example.dog.service;

import com.example.dog.model.Dog;

public interface DogService {
    Dog get(int id);
    Dog add(Dog dog);
    Dog update(int id, Dog dog);
    Dog remove(int id);
}
