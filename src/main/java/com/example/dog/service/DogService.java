package com.example.dog.service;

import com.example.dog.dao.DogDAO;
import com.example.dog.dao.JdbcConnectionHolder;
import com.example.dog.dao.ObjectNotFoundException;
import com.example.dog.model.Dog;

public class DogService {

    private final DogDAO dao;
    private final JdbcConnectionHolder connections;

    public DogService(DogDAO dao, JdbcConnectionHolder connections) {
        this.dao = dao;
        this.connections = connections;
    }

    public Dog get(int id) {
        connections.getConnection();
        connections.setReadOnly(true);
        return dao.get(id);
    }

    public Dog add(Dog dog) {
        connections.startTransaction();
        Dog addedDog = dao.add(dog);
        connections.commit();
        connections.close();
        return addedDog;
    }

    public Dog update(int id, Dog dog) {
        connections.startTransaction();
        Dog updatedDog;
        try {
            updatedDog = dao.update(id, dog);
            connections.commit();
        } catch (ObjectNotFoundException e) {
            connections.rollback();
            throw e;
        } finally {
            connections.close();
        }
        return updatedDog;
    }

    public Dog remove(int id) {
        connections.startTransaction();
        Dog deletedDog = dao.remove(id);
        connections.commit();
        connections.close();
        return deletedDog;
    }
}
