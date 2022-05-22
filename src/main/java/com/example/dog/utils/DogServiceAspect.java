package com.example.dog.utils;

import com.example.dog.dao.JdbcConnectionHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.annotation.Annotation;

public class DogServiceAspect {

    private final JdbcConnectionHolder jdbcConnectionHolder;

    public DogServiceAspect(JdbcConnectionHolder jdbcConnectionHolder) {
        this.jdbcConnectionHolder = jdbcConnectionHolder;
    }

    public Object addTransactionalSupport(ProceedingJoinPoint pjp, JoinPoint jp) throws Throwable {
        Object result;
        Annotation[] annotations = jp.getSignature().getDeclaringType().getAnnotations();
        if(annotations.length != 0) {
            for (Annotation ann : annotations) {
                if (ann.toString().equals("CustomTransactional")) {
                    jdbcConnectionHolder.setReadOnly(true);
                }
            }
        }
        jdbcConnectionHolder.startTransaction();
        try {
            result = pjp.proceed();
            jdbcConnectionHolder.commit();
            return result;
        } catch (Throwable e) {
            jdbcConnectionHolder.rollback();
            throw e;
        } finally {
            jdbcConnectionHolder.close();
        }
    }
}
