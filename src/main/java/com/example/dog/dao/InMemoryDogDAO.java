package com.example.dog.dao;

import com.example.dog.model.Dog;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDogDAO implements DogDAO {
    private static Map<Integer, Dog> dogs = new ConcurrentHashMap<>();
    private static AtomicInteger id = new AtomicInteger(0);

    @Override
    public Dog get(int id) {
        return dogs.get(id);
    }

    @Override
    public Dog add(Dog dog) {
        id.incrementAndGet();
        dog.setId(id.get());
        dogs.put(dog.getId(), dog);
        return dog;
    }

    @Override
    public Dog update(int id, Dog dog) {
        Dog newValue = dogs.computeIfPresent(id, (k, v)-> dog);
        if (newValue != null) {
            newValue.setId(id);
        }
        return newValue;
    }

    @Override
    public Dog remove(int id) {
        return dogs.remove(id);
    }
}
