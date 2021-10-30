package com.example.dog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class SpringContextTest {

    @Autowired
    private DogController dogController;

//    @Autowired
//    private House house;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(dogController);
    }

//    @Test
//    public void contextLoads2() throws Exception {
//        assertNotNull(house);
//    }
}
