package com.example.dog;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HouseConfig {

    @Bean
    public House house() {
        return new House();
    }
}
