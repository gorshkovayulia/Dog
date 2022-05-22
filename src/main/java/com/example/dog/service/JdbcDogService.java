package com.example.dog.service;

import com.example.dog.dao.DogDAO;
import com.example.dog.dao.ObjectNotFoundException;
import com.example.dog.model.Dog;
import com.example.dog.utils.CustomTransactional;

public class JdbcDogService implements DogService {

    private final DogDAO dao;

    public JdbcDogService(DogDAO dao) {
        this.dao = dao;
    }

    @Override
    @CustomTransactional
    public Dog get(int id) {
        return dao.get(id);
    }

    @Override
    @CustomTransactional
    public Dog add(Dog dog) {
        return dao.add(dog);
    }

    @Override
    @CustomTransactional
    public Dog update(int id, Dog dog) {
        return dao.update(id, dog);
    }

    @Override
    @CustomTransactional
    public Dog remove(int id) {
//        if(get(id) == null) // TODO: Will this be transactional? How to make it transactional?
//            throw new ObjectNotFoundException(null);
        return dao.remove(id);
    }
}
