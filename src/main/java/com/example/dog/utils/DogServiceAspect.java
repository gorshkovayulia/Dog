package com.example.dog.utils;

import com.example.dog.dao.JdbcConnectionHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

public class DogServiceAspect {

    private final JdbcConnectionHolder jdbcConnectionHolder;

    public DogServiceAspect(JdbcConnectionHolder jdbcConnectionHolder) {
        this.jdbcConnectionHolder = jdbcConnectionHolder;
    }

    public Object addTransactionalSupport(ProceedingJoinPoint pjp, JoinPoint jp) throws Throwable {
        Object result;
        if (jp.getSignature().getName().startsWith("get")) {
            jdbcConnectionHolder.setReadOnly(true);
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
