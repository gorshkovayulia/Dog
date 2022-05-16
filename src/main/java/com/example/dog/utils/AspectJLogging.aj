package com.example.dog.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public aspect AspectJLogging {

    private static final Log LOG = LogFactory.getLog(AspectJLogging.class);

    pointcut loggableAnnotatedMethods(): execution(* com.example.dog.service.DogService.get(..));

    Object before(): loggableAnnotatedMethods() {
        String signature = thisJoinPoint.getSignature().getName();
        LOG.info("Before " + signature);
    }
}