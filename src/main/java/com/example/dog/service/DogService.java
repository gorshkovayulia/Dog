package com.example.dog.service;

import com.example.dog.model.Dog;
import com.example.dog.utils.CustomTransactional;

public interface DogService {

    @CustomTransactional
    Dog get(int id);

    Dog add(Dog dog);

    Dog update(int id, Dog dog);

    Dog remove(int id);
}
