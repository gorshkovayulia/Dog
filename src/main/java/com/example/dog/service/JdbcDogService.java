package com.example.dog.service;

import com.example.dog.dao.DogDAO;
import com.example.dog.model.Dog;
import org.springframework.transaction.annotation.Transactional;

public class JdbcDogService implements DogService {

    private final DogDAO dao;

    public JdbcDogService(DogDAO dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public Dog get(int id) {
        return dao.get(id);
    }

    @Override
    @Transactional
    public Dog add(Dog dog) {
        return dao.add(dog);
    }

    @Override
    @Transactional
    public Dog update(int id, Dog dog) {
        return dao.update(id, dog);
    }

    @Override
    @Transactional
    public Dog remove(int id) {
//        if(get(id) == null) // TODO: Will this be transactional? How to make it transactional?
//            throw new ObjectNotFoundException(null);
        return dao.remove(id);
    }
}
