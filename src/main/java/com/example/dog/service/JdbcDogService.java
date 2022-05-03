package com.example.dog.service;

import com.example.dog.dao.DogDAO;
import com.example.dog.model.Dog;

public class JdbcDogService implements DogService {

    private final DogDAO dao;

    public JdbcDogService(DogDAO dao) {
        this.dao = dao;
    }

    @Override
    public Dog get(int id) {
        return dao.get(id);
    }

    @Override
    public Dog add(Dog dog) {
        return dao.add(dog);
    }

    @Override
    public Dog update(int id, Dog dog) {
        return dao.update(id, dog);
    }

    @Override
    public Dog remove(int id) {
        return dao.remove(id);
    }
}
