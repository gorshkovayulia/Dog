package com.example.dog.utils;

public aspect AspectJLogging {

    pointcut loggableAnnotatedMethods(): execution(* com.example.dog.service.DogService.get(..));

    before(): loggableAnnotatedMethods() {
        String signature = thisJoinPoint.getSignature().getName();
        System.out.println("BEFORE " + signature);
    }
}