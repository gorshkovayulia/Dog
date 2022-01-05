package com.example.dog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/controllers-context.xml")
public class SpringContextTest {

    @Autowired
    private DogController dogController;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(dogController);
    }
}
