package com.example.dog.service;

import com.example.dog.dao.DogDAO;
import com.example.dog.model.Dog;

public class DogService {

    private final DogDAO dao;

    public DogService(DogDAO dao) {
        this.dao = dao;
    }

    public Dog get(int id) {
        return dao.get(id);
    }

    public Dog add(Dog dog) {
        return dao.add(dog);
    }

    public Dog update(int id, Dog dog) {
        return dao.update(id, dog);
    }

    public Dog remove(int id) {
        return dao.remove(id);
    }
}
