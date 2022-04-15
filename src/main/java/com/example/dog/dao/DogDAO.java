package com.example.dog.dao;

import com.example.dog.model.Dog;

public interface DogDAO {

    Dog get(int id);
    Dog add(Dog dog);
    Dog update(int id, Dog dog);
    Dog remove(int id);

}
